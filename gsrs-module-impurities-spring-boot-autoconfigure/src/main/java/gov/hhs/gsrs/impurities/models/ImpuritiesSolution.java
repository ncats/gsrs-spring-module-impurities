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
@Table(name="SRSCID_IMPURITIES_SOLUTION")
public class ImpuritiesSolution extends ImpuritiesCommonData {

    @Id
    @SequenceGenerator(name = "impSolSeq", sequenceName = "SRSCID_SQ_IMPURITIES_SOLUTION_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impSolSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "SOLUTION_DESCRIPTION", length=4000)
    public String solutionDescription;

    @Column(name = "SOLUTION_LETTER", length=4000)
    public String solutionLetter;

    // Set PARENT Class, ImpuritiesTesting
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="IMPURITIES_TEST_ID")
    public ImpuritiesTesting owner;

    // Set PARENT Class, ImpuritiesTesting
    public void setOwner(ImpuritiesTesting impuritiesTesting) {
        this.owner = impuritiesTesting;
    }

}
