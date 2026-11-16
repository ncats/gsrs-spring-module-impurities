package gov.hhs.gsrs.impurities.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import gov.hhs.gsrs.impurities.models.*;
import gov.hhs.gsrs.impurities.services.*;
import gov.hhs.gsrs.impurities.LegacyImpuritiesSearcher;
import gov.hhs.gsrs.impurities.ImpuritiesDataSourceConfig;

import gov.nih.ncats.common.util.Unchecked;
import gsrs.GsrsFactoryConfiguration;
import gsrs.autoconfigure.GsrsExportConfiguration;
import gsrs.controller.*;
import gsrs.repository.ETagRepository;
import gsrs.repository.EditRepository;
import gsrs.security.canApproveRecords;
import gsrs.service.EtagExportGenerator;
import gsrs.service.ExportService;
import gsrs.service.GsrsEntityService;
import ix.core.models.ETag;
import gsrs.legacy.LegacyGsrsSearchService;
import ix.core.search.SearchOptions;
import ix.core.search.SearchResult;
import ix.core.search.text.TextIndexer;
import ix.ginas.exporters.ExportMetaData;
import ix.ginas.exporters.ExportProcess;
import ix.ginas.exporters.Exporter;
import ix.ginas.exporters.ExporterFactory;
import ix.ginas.models.v1.Substance;
import gsrs.controller.hateoas.HttpRequestHolder;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

@ExposesResourceFor(Impurities.class)
@GsrsRestApiController(context = ImpuritiesEntityService.CONTEXT, idHelper = IdHelpers.NUMBER)
public class ImpuritiesController extends EtagLegacySearchEntityController<ImpuritiesController, Impurities, Long> {


    @Autowired
    private ETagRepository eTagRepository;

    @PersistenceContext(unitName = ImpuritiesDataSourceConfig.NAME_ENTITY_MANAGER)
    private EntityManager entityManager;

    @Autowired
    private GsrsControllerConfiguration gsrsControllerConfiguration;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ExportService exportService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private GsrsExportConfiguration gsrsExportConfiguration;

    @Autowired
    private ImpuritiesEntityService impuritiesEntityService;

    @Autowired
    private SubstanceModuleService substanceModuleService;

    @Autowired
    private LegacyImpuritiesSearcher legacyImpuritiesSearcher;

    @Autowired
    private GsrsFactoryConfiguration gsrsFactoryConfiguration;

    @Override
    public GsrsEntityService<Impurities, Long> getEntityService() {
        return impuritiesEntityService;
    }

    @Override
    protected LegacyGsrsSearchService<Impurities> getlegacyGsrsSearchService() {
        return legacyImpuritiesSearcher;
    }

    @Override
    protected Stream<Impurities> filterStream(Stream<Impurities> stream, boolean publicOnly, Map<String, String> parameters) {
        return stream;
    }

    @Override
    public SearchOptions instrumentSearchOptions(SearchOptions so) {

        so = super.instrumentSearchOptions(so);

        so.addDateRangeFacet("root_creationDate");
        so.addDateRangeFacet("root_lastModifiedDate");

        if (gsrsFactoryConfiguration != null) {
            Optional<Map<String, Object>> conf = gsrsFactoryConfiguration
                    .getSearchSettingsFor(ImpuritiesEntityService.CONTEXT);

            String restrict = conf
                    .map(cc -> cc.get("restrictDefaultToIdentifiers"))
                    .filter(bb -> bb != null).map(bb -> bb.toString())
                    .orElse(null);
            if (restrict != null && "true".equalsIgnoreCase(restrict)) {
                so.setDefaultField(TextIndexer.FULL_IDENTIFIER_FIELD);
            }
        }
        return so;
    }

    @GetGsrsRestApiMapping("/subRelationship/{substanceUuid}")
    public ResponseEntity<String> findSubstanceRelationshipBySubstanceUuid(@PathVariable("substanceUuid") String substanceUuid) throws Exception {
        List<SubstanceRelationship> list = impuritiesEntityService.findSubstanceRelationshipBySubstanceUuid(substanceUuid);
        if (substanceUuid == null) {
            throw new IllegalArgumentException("There is no Substance Id provided in findSubstanceRelationshipBySubstanceUuid");
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/getImpuritiesLists")
    public ResponseEntity<String> getImpuritiesLists() throws Exception {
        List<Object[]> list = impuritiesEntityService.getImpuritiesLists();

        return new ResponseEntity(list, HttpStatus.OK);
    }

}
