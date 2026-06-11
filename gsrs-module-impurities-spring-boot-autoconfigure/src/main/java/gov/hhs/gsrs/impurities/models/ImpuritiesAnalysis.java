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
@Table(name="SRSCID_IMPURITIES_ANALYSIS")
public class ImpuritiesAnalysis extends ImpuritiesCommonData {

    @Id
    @SequenceGenerator(name = "impAnalySeq", sequenceName = "SRSCID_SQ_IMPURITIES_ANALY_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impAnalySeq")
    @Column(name = "ID")
    public Long id;

    @Indexable
    @Column(name = "SAMPLES", length=500)
    public String samples;

    @Column(name = "DESCRIPTION", length=2000)
    public String description;

    @Column(name = "RESULT_FORMULA", length=500)
    public String resultFormula;

    @Column(name = "FORMULA_RU", length=500)
    public String formulaRU;

    @Column(name = "FORMULA_RS", length=500)
    public String formulaRS;

    @Column(name = "FORMULA_CS", length=500)
    public String formulaCS;

    @Column(name = "FORMULA_CU", length=500)
    public String formulaCU;

    @Column(name = "FORMULA_P", length=500)
    public String formulaP;

    @Column(name = "FORMULA_F", length=500)
    public String formulaF;

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

}
