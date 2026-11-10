package gov.hhs.gsrs.impurities.models;

import ix.core.SingleParent;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@SingleParent
@Data
@Entity
@Table(name="SRSCID_IMPURITIES_TOTAL")
public class ImpuritiesTotal extends ImpuritiesCommonData {
    @Id
    @SequenceGenerator(name = "impTotalSeq", sequenceName = "SRSCID_SQ_IMPURITIES_TOTAL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impTotalSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "TEST_TYPE", length=500)
    public String testType;

    @Column(name = "LIMIT_TYPE", length=500)
    public String limitType;

    @Column(name = "LIMIT_VALUE", length=500)
    public String limitValue;

    @Column(name = "UNIT", length=500)
    public String unit;

    @Column(name = "COMMENTS", length=4000)
    public String comments;
}
