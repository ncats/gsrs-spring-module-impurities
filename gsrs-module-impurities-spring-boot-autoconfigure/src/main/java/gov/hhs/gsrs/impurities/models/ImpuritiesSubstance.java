package gov.hhs.gsrs.impurities.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name="SRSCID_IMPURITIES_SUBSTANCE")
public class ImpuritiesSubstance extends ImpuritiesCommonData {

    @Id
    @SequenceGenerator(name = "impSubSeq", sequenceName = "SRSCID_SQ_IMPURITIES_SUBS_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impSubSeq")
    @Column(name = "ID")
    public Long id;

    @Indexable
    @Column(name = "SUBSTANCE_UUID")
    public String substanceUuid;

    @Indexable
    @Column(name = "APPROVAL_ID")
    public String approvalID;

    @Column(name="LOW")
    public Double low;

    @Column(name="HIGH")
    public Double high;

    @Column(name="UNIT")
    public String unit;

    @Column(name="COMMENTS")
    public String comments;

    // Set PARENT Class, Impurities
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="IMPURITIES_ID")
    public Impurities owner;

    // Set PARENT Class, Impurities
    public void setOwner(Impurities impurities) {
        this.owner = impurities;
    }

    // Set Children Class, ImpuritiesTesting
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesTesting> impuritiesTestList = new ArrayList<ImpuritiesTesting>();

    // Set Children Class, ImpuritiesTesting
    public void setImpuritiesTestList(List<ImpuritiesTesting> impuritiesTestList) {
        this.impuritiesTestList = impuritiesTestList;
        if (impuritiesTestList !=null) {
            for (ImpuritiesTesting imp : impuritiesTestList)
            {
                imp.setOwner(this);
            }
        }
    }

    // Set Children Class, ImpuritiesResidualSolventsTest
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesResidualSolventsTest> impuritiesResidualSolventsTestList = new ArrayList<ImpuritiesResidualSolventsTest>();

    // Set Children Class, ImpuritiesResidualSolventsTest
    public void setImpuritiesResidualSolventsTestList(List<ImpuritiesResidualSolventsTest> impuritiesResidualSolventsTestList) {
        this.impuritiesResidualSolventsTestList = impuritiesResidualSolventsTestList;
        if (impuritiesResidualSolventsTestList != null) {
            for (ImpuritiesResidualSolventsTest imp : impuritiesResidualSolventsTestList)
            {
                imp.setOwner(this);
            }
        }
    }

    // Set Children Class, ImpuritiesInorganicTest
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesInorganicTest> impuritiesInorganicTestList = new ArrayList<ImpuritiesInorganicTest>();

    // Set Children Class, ImpuritiesInorganicTest
    public void setImpuritiesInorganicTestList(List<ImpuritiesInorganicTest> impuritiesInorganicTestList) {
        this.impuritiesInorganicTestList = impuritiesInorganicTestList;
        if (impuritiesInorganicTestList != null) {
            for (ImpuritiesInorganicTest imp : impuritiesInorganicTestList)
            {
                imp.setOwner(this);
            }
        }
    }

    /*
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesResidualSolvents> impuritiesResidualSolventsList = new ArrayList<ImpuritiesResidualSolvents>();

    public void setImpuritiesResidualSolventsList(List<ImpuritiesResidualSolvents> impuritiesResidualSolventsList) {
        this.impuritiesResidualSolventsList = impuritiesResidualSolventsList;
        if(impuritiesResidualSolventsList !=null) {
            for (ImpuritiesResidualSolvents imp : impuritiesResidualSolventsList)
            {
                imp.setOwner(this);
            }
        }
    }

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesInorganic> impuritiesInorganicList = new ArrayList<ImpuritiesInorganic>();

    public void setImpuritiesInorganicList(List<ImpuritiesInorganic> impuritiesInorganicList) {
        this.impuritiesInorganicList = impuritiesInorganicList;
        if(impuritiesInorganicList !=null) {
            for (ImpuritiesInorganic imp : impuritiesInorganicList)
            {
                imp.setOwner(this);
            }
        }
    }
    */
}
