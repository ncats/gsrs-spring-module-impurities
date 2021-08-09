package gov.nih.ncats.impurities;

import gov.nih.ncats.impurities.models.Impurities;
import gov.nih.ncats.impurities.repositories.ImpuritiesRepository;

import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.GsrsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyImpuritiesSearcher extends LegacyGsrsSearchService<Impurities> {

    @Autowired
    public LegacyImpuritiesSearcher(ImpuritiesRepository repository) {
        super(Impurities.class, repository);
    }
}
