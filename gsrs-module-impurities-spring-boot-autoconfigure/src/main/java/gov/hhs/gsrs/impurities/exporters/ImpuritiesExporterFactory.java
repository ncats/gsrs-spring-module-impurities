package gov.hhs.gsrs.impurities.exporters;

import gov.hhs.gsrs.impurities.models.*;
import gov.hhs.gsrs.impurities.controllers.*;

import gsrs.DefaultDataSourceConfig;
import gsrs.springUtils.AutowireHelper;
import ix.ginas.exporters.*;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;;
import java.util.*;

public class ImpuritiesExporterFactory implements ExporterFactory {

	@PersistenceContext(unitName =  DefaultDataSourceConfig.NAME_ENTITY_MANAGER)
	public EntityManager entityManager;

	@Autowired
	public ImpuritiesController impuritiesController;

	private static final Set<OutputFormat> FORMATS;

	static {
		Set<OutputFormat> set = new LinkedHashSet<>();
		set.add(SpreadsheetFormat.TSV);
		set.add(SpreadsheetFormat.CSV);
		set.add(SpreadsheetFormat.XLSX);

		FORMATS = Collections.unmodifiableSet(set);
	}

	@Override
	public Set<OutputFormat> getSupportedFormats() {
		return FORMATS;
	}

	@Override
	public boolean supports(Parameters params) {
		return params.getFormat() instanceof SpreadsheetFormat;
	}

	@Override
	public ImpuritiesExporter createNewExporter(OutputStream out, Parameters params) throws IOException {

		if (impuritiesController == null) {
			AutowireHelper.getInstance().autowire(this);
		}

		SpreadsheetFormat format = SpreadsheetFormat.XLSX;
		Spreadsheet spreadsheet = format.createSpeadsheet(out);

		ImpuritiesExporter.Builder builder = new ImpuritiesExporter.Builder(spreadsheet);
		configure(builder, params);
		
		return builder.build(impuritiesController, entityManager);
	}

	protected void configure(ImpuritiesExporter.Builder builder, Parameters params) {
		builder.includePublicDataOnly(params.publicOnly());
	}

}