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
@Table(name="SRSCID_IMPURITIES_IDENTITY")
public class ImpuritiesIdentityCriteria extends ImpuritiesCommonData {

    @Id
    @SequenceGenerator(name = "impIdentSeq", sequenceName = "SRSCID_SQ_IMPURITIES_IDENT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impIdentSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "IDENTITY_CRITERIA_TYPE", length=500)
    public String identityCriteriaType;

    @Column(name = "AMOUNT_VALUE", length=500)
    public String amountValue;

    @Column(name = "UNIT", length=500)
    public String unit;

    @Indexable(indexed=false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="IMPURITIES_UNSPECIFIED_ID")
    public ImpuritiesUnspecified owner;

    public void setOwner(ImpuritiesUnspecified impuritiesUnspecified) {
        if (impuritiesUnspecified instanceof ImpuritiesUnspecified) {
            this.owner = impuritiesUnspecified;
        }
    }

    @Indexable(indexed=false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="IMPURITIES_DETAILS_ID")
    public ImpuritiesDetails ownerDetails;

    public void setOwnerDetails(ImpuritiesDetails impuritiesDetails) {
        this.ownerDetails = impuritiesDetails;
    }

    @ParentReference
    public void setAppropriateOwner(Object obj) {
        if (obj instanceof ImpuritiesUnspecified) {
            this.owner = (ImpuritiesUnspecified) obj;
        } else if (obj instanceof ImpuritiesDetails) {
            this.ownerDetails= (ImpuritiesDetails) obj;
        }
    }
}
