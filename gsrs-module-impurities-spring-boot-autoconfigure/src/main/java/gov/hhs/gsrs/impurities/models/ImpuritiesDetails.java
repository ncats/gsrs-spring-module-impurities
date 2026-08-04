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

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@SingleParent
@Data
@Entity
@Table(name="SRSCID_IMPURITIES_DETAILS")
public class ImpuritiesDetails extends ImpuritiesCommonData {

    @Id
    @SequenceGenerator(name = "impDetSeq", sequenceName = "SRSCID_SQ_IMPURITIES_DETAIL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impDetSeq")
    @Column(name = "ID")
    public Long id;

    @Indexable
    @Column(name = "RELATED_SUBSTANCE_UUID", length=500)
    public String relatedSubstanceUuid;

    @Column(name = "IMPURITY_TYPE", length=500)
    public String impurityType;

    @Column(name = "COMMENTS", length=4000)
    public String comments;

    @Column(name = "TEST_TYPE", length=500)
    public String testType;

    @Column(name = "SOURCE_IMPURITY_NAME", length=1000)
    public String sourceImpurityName;

    @Column(name = "LIMIT_VALUE", length=500)
    public String limitValue;

    @Column(name = "LIMIT_TYPE", length=500)
    public String limitType;

    @Column(name = "UNIT", length=500)
    public String unit;

    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="IMPURITIES_TEST_ID")
    public ImpuritiesTesting owner;

    public void setOwner(ImpuritiesTesting impuritiesTesting) {
        this.owner = impuritiesTesting;
    }

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ownerDetails")
    public List<ImpuritiesIdentityCriteria> identityCriteriaList = new ArrayList<ImpuritiesIdentityCriteria>();

    public void setIdentityCriteriaList(List<ImpuritiesIdentityCriteria> identityCriteriaList) {
        this.identityCriteriaList = identityCriteriaList;
        if(identityCriteriaList !=null) {
            for (ImpuritiesIdentityCriteria imp : identityCriteriaList)
            {
                imp.setAppropriateOwner(this);
            }
        }
    }
}
