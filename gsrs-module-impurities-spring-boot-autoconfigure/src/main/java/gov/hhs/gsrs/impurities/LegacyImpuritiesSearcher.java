package gov.hhs.gsrs.impurities;

import gov.hhs.gsrs.impurities.models.Impurities;
import gov.hhs.gsrs.impurities.repositories.ImpuritiesRepository;

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
