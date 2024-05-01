package gov.hhs.gsrs.impurities.models;

import gsrs.GsrsEntityProcessorListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import ix.core.models.Indexable;
import ix.core.models.IxModel;
import ix.core.SingleParent;
import ix.core.models.ParentReference;
import ix.core.search.text.TextIndexerEntityListener;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@SingleParent
@Data
@Entity
@Table(name="SRSCID_IMPURITIES_TEST")
public class ImpuritiesTesting extends ImpuritiesCommonData {

    @Id
    @SequenceGenerator(name = "impTestSeq", sequenceName = "SRSCID_SQ_IMPURITIES_TEST_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impTestSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "TEST", length=1000)
    public String test;

    @Column(name = "TEST_TYPE", length=150)
    public String testType;

    @Column(name = "TEST_DESCRIPTION", length=4000)
    public String testDescription;

    @Column(name = "COMMENTS", length=4000)
    public String comments;

    // added following New 18 fields in 3.1
    @Column(name = "SOURCE_TYPE", length=500)
    public String sourceType;

    @Column(name = "SOURCE", length=500)
    public String source;

    @Column(name = "SOURCE_ID", length=500)
    public String sourceId;

    @Column(name = "TEST_SYSTEM", length=150)
    public String system;

    @Column(name = "TEST_MODE", length=150)
    public String mode;

    @Column(name = "DETECTION_TYPE", length=150)
    public String detectionType;

    @Column(name = "DETECTION_DETAILS", length=500)
    public String detectionDetails;

    @Column(name = "COLUMN_PACKING_TYPE", length=150)
    public String columnPackingType;

    @Column(name = "COLUMN_PACKING_SIZE", length=200)
    public String columnPackingSize;

    @Column(name = "COLUMN_SIZE", length=200)
    public String columnSize;

    @Column(name = "COLUMN_TEMPERATURE", length=200)
    public String columnTemperature;

    @Column(name = "FLOW_RATE", length=200)
    public String flowRate;

    @Column(name = "INJECTION_VOLUME_AMOUNT", length=200)
    public String injectionVolumeAmount;

    @Column(name = "DILUENT", length=500)
    public String diluent;

    @Column(name = "STANDARD_SOLUTION", length=500)
    public String standardSolution;

    @Column(name = "SAMPLE_SOLUTION", length=500)
    public String sampleSolution;

    @Column(name = "SYSTEM_SUITABILITY_SOLUTION", length=500)
    public String systemSuitabilitySolution;

    @Column(name = "OTHER_SOLUTION", length=2000)
    public String otherSolution;

    @Column(name = "SOLUTION_A_DESCRIPTION", length=2000)
    public String solutionADescription;

    @Column(name = "SOLUTION_B_DESCRIPTION", length=2000)
    public String solutionBDescription;

    // Suitability Requirements Resolution
    @Column(name = "SUITABILITY_REQ_RESOLUTION", length=500)
    public String suitabilityReqResolution;

    // Suitability Requirements Relative Standard Deviation
    @Column(name = "SUITABILITY_REQ_REL_STAND_DEV", length=200)
    public String suitabilityReqRelStandardDeviation;

    @Column(name = "ELUTION_TYPE", length=200)
    public String elutionType;

    // Set PARENT Class, ImpuritiesSubstance
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="IMPURITIES_SUBSTANCE_ID")
    public ImpuritiesSubstance owner;

    // Set PARENT Class, ImpuritiesSubstance
    public void setOwner(ImpuritiesSubstance impuritiesSubstance) {
        this.owner = impuritiesSubstance;
    }

    // Set CHILDREN Class, ImpuritiesElutionSolvent
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesElutionSolvent> impuritiesElutionSolventList = new ArrayList<ImpuritiesElutionSolvent>();

    // Set CHILDREN Class, ImpuritiesElutionSolvent
    public void setImpuritiesElutionSolventList(List<ImpuritiesElutionSolvent> impuritiesElutionSolventList) {
        this.impuritiesElutionSolventList = impuritiesElutionSolventList;
        if (impuritiesElutionSolventList != null) {
            for (ImpuritiesElutionSolvent imp : impuritiesElutionSolventList)
            {
                imp.setOwner(this);
            }
        }
    }

    // Set CHILDREN Class, ImpuritiesSolutionTable
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesSolutionTable> impuritiesSolutionTableList = new ArrayList<ImpuritiesSolutionTable>();

    // Set CHILDREN Class, ImpuritiesSolutionTable
    public void setImpuritiesSolutionTableList(List<ImpuritiesSolutionTable> impuritiesSolutionTableList) {
        this.impuritiesSolutionTableList = impuritiesSolutionTableList;
        if (impuritiesSolutionTableList != null) {
            for (ImpuritiesSolutionTable imp : impuritiesSolutionTableList)
            {
                imp.setOwner(this);
            }
        }
    }

    // Set CHILDREN Class, ImpuritiesDetails
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesDetails> impuritiesDetailsList = new ArrayList<ImpuritiesDetails>();

    // Set CHILDREN Class, ImpuritiesDetails
    public void setImpuritiesDetailsList(List<ImpuritiesDetails> impuritiesDetailsList) {
        this.impuritiesDetailsList = impuritiesDetailsList;
        if (impuritiesDetailsList != null) {
            for (ImpuritiesDetails imp : impuritiesDetailsList)
            {
                imp.setOwner(this);
            }
        }
    }

    // Set CHILDREN Class, ImpuritiesUnspecified
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesUnspecified> impuritiesUnspecifiedList = new ArrayList<ImpuritiesUnspecified>();

    // Set CHILDREN Class, ImpuritiesUnspecified
    public void setImpuritiesUnspecifiedList(List<ImpuritiesUnspecified> impuritiesUnspecifiedList) {
        this.impuritiesUnspecifiedList = impuritiesUnspecifiedList;
        if (impuritiesUnspecifiedList != null) {
            for (ImpuritiesUnspecified imp : impuritiesUnspecifiedList)
            {
                imp.setOwner(this);
            }
        }
    }

}
