package gov.hhs.gsrs.impurities.repositories;

import gov.hhs.gsrs.impurities.models.*;

import gsrs.repository.GsrsRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ImpuritiesRepository extends GsrsRepository<Impurities, Long> {
    Optional<Impurities> findById(Long id);

    @Query("SELECT a FROM SubstanceRelationship a WHERE a.substanceId = ?1")
    List<SubstanceRelationship> findSubstanceRelationshipBySubstanceUuid(String substanceUuid);
}



