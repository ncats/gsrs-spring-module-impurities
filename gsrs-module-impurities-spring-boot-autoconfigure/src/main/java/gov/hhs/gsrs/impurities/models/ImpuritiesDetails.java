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

import javax.persistence.EntityListeners;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

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
    @Column(name = "RELATED_SUBSTANCE_UUID")
    public String relatedSubstanceUuid;

    @Column(name = "IMPURITY_TYPE")
    public String impurityType;

    @Column(name = "COMMENTS")
    public String comments;

    @Column(name = "TEST_TYPE")
    public String testType;

    @Column(name = "SOURCE_IMPURITY_NAME")
    public String sourceImpurityName;

    @Column(name = "LIMIT_VALUE")
    public String limitValue;

    @Column(name = "LIMIT_TYPE")
    public String limitType;

    @Column(name = "UNIT")
    public String unit;

    /*
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
    */

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
