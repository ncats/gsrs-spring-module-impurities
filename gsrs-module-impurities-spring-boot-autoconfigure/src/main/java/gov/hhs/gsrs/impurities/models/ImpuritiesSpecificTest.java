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
@Table(name="SRSCID_IMPURITIES_SPECIFIC_TEST")
public class ImpuritiesSpecificTest extends ImpuritiesCommonData {

    @Id
    @SequenceGenerator(name = "impSpecTestSeq", sequenceName = "SRSCID_SQ_IMPURITIES_SPEC_TEST_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impSpecTestSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "TEST_TYPE", length=500)
    public String testType;

    @Column(name = "LIMIT_TYPE", length=500)
    public String limitType;

    @Column(name = "LIMIT_VALUE", length=500)
    public String limitValue;

    @Column(name = "UNIT", length=500)
    public String unit;

    @Column(name = "SAMPLE_SOLUTION", length=2000)
    public String sampleSolution;

    @Column(name = "ANALYSIS", length=2000)
    public String analysis;

    @Column(name = "ACCEPTANCE_CRITERIA", length=2000)
    public String acceptanceCriteria;

    @Column(name = "OTHER_DESCRIPTION", length=2000)
    public String otherDescription;

    @Column(name = "COMMENTS", length=4000)
    public String comments;

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

}
