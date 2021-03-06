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
public class ImpuritiesTesting extends AbstractGsrsEntity {

    @Id
    @SequenceGenerator(name = "impTestSeq", sequenceName = "SRSCID_SQ_IMPURITIES_TEST_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impTestSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "TEST")
    public String test;

    @Column(name = "TEST_TYPE")
    public String testType;

    @Column(name = "TEST_DESCRIPTION")
    public String testDescription;

    @Column(name = "COMMENTS")
    public String comments;

    @Version
    public Long internalVersion;

    @Column(name = "CREATED_BY")
    public String createdBy;

    @Column(name = "MODIFIED_BY")
    public String modifiedBy;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @CreatedDate
    @Indexable( name = "Create Date", sortable=true)
    @Column(name = "CREATE_DATE")
    private Date creationDate;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @LastModifiedDate
    @Indexable( name = "Last Modified Date", sortable=true)
    @Column(name = "MODIFY_DATE")
    private Date lastModifiedDate;

    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="IMPURITIES_SUBSTANCE_ID")
    public ImpuritiesSubstance owner;

    public void setOwner(ImpuritiesSubstance impuritiesSubstance) {
        this.owner = impuritiesSubstance;
    }

    /*
    @Indexable(indexed=false)
    @JsonIgnore
    @ManyToOne
   // @JoinColumn(name="IMPURITIES_SUBSTANCE_ID")
    public ImpuritiesSubstance impuritiesTestFromSub;
    */

   // @JoinColumn(name = "IMPURITIES_TEST_ID", referencedColumnName = "ID")
   // @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   // public List<ImpuritiesDetails> impuritiesDetailsList = new ArrayList<ImpuritiesDetails>();

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesDetails> impuritiesDetailsList = new ArrayList<ImpuritiesDetails>();

    public void setImpuritiesDetailsList(List<ImpuritiesDetails> impuritiesDetailsList) {
        this.impuritiesDetailsList = impuritiesDetailsList;
        if(impuritiesDetailsList !=null) {
            for (ImpuritiesDetails imp : impuritiesDetailsList)
            {
                imp.setOwner(this);
            }
        }
    }

  //  @LazyCollection(LazyCollectionOption.FALSE)
  //  @JoinColumn(name = "IMPURITIES_TEST_ID", referencedColumnName = "ID")
  //  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  //  public List<ImpuritiesUnspecified> impuritiesUnspecifiedList = new ArrayList<ImpuritiesUnspecified>();

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesUnspecified> impuritiesUnspecifiedList = new ArrayList<ImpuritiesUnspecified>();

    public void setImpuritiesUnspecifiedList(List<ImpuritiesUnspecified> impuritiesUnspecifiedList) {
        this.impuritiesUnspecifiedList = impuritiesUnspecifiedList;
        if(impuritiesUnspecifiedList !=null) {
            for (ImpuritiesUnspecified imp : impuritiesUnspecifiedList)
            {
                imp.setOwner(this);
            }
        }
    }

}
