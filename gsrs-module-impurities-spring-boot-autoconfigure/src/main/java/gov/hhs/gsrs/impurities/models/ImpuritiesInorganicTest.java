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
@Table(name="SRSCID_IMPURITIES_INORGAN_TEST")
public class ImpuritiesInorganicTest extends ImpuritiesCommonData {

    @Id
    @SequenceGenerator(name = "impInorganicTestSeq", sequenceName = "SRSCID_SQ_IMPURITIES_I_TEST_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impInorganicTestSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "SOURCE_TYPE", length=500)
    public String sourceType;

    @Column(name = "SOURCE", length=500)
    public String source;

    @Column(name = "SOURCE_ID", length=500)
    public String sourceId;

    @Column(name = "TEST", length=1000)
    public String test;

    @Column(name = "TEST_TYPE", length=500)
    public String testType;

    @Column(name = "TEST_DESCRIPTION", length=4000)
    public String testDescription;

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

    // Set CHILDREN Class, ImpuritiesInorganic
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesInorganic> impuritiesInorganicList = new ArrayList<ImpuritiesInorganic>();

    // Set CHILDREN Class, ImpuritiesInorganic
    public void setImpuritiesInorganicList(List<ImpuritiesInorganic> impuritiesInorganicList) {
        this.impuritiesInorganicList = impuritiesInorganicList;
        if(impuritiesInorganicList !=null) {
            for (ImpuritiesInorganic imp : impuritiesInorganicList)
            {
                imp.setOwner(this);
            }
        }
    }
}
