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
