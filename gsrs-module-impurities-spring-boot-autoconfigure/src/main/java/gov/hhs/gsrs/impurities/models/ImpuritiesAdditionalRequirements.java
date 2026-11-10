package gov.hhs.gsrs.impurities.models;

import ix.core.SingleParent;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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
