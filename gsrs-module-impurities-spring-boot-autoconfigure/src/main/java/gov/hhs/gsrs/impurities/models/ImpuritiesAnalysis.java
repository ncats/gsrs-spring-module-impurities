package gov.hhs.gsrs.impurities.models;

import ix.core.models.Indexable;
import ix.core.SingleParent;
import ix.core.models.ParentReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


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

    @Column(name = "FORMULA_MR1", length=500)
    public String formulaMR1;

    @Column(name = "FORMULA_MR2", length=500)
    public String formulaMR2;

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
