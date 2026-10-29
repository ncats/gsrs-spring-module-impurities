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

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@SingleParent
@Data
@Entity
@Table(name="SRSCID_IMPURITIES_ADDL_REQUIREMENTS")
public class ImpuritiesAdditionalRequirements extends ImpuritiesCommonData {
    @Id
    @SequenceGenerator(name = "impAddReqSeq", sequenceName = "SRSCID_SQ_IMPURITIES_ADDL_REQ_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impAddReqSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "REFERENCE_STANDARDS", length=4000)
    public String referenceStandards;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMPURITIES_ID")
    @JsonIgnore
    private Impurities owner;

    public void setOwner(Impurities owner) {
        this.owner = owner;
    }

    public Impurities getOwner() {
        return this.owner;
    }
}
