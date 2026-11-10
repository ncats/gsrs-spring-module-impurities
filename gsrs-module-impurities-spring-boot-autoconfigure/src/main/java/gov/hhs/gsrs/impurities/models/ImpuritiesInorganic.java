package gov.hhs.gsrs.impurities.models;

import ix.core.models.Indexable;
import ix.core.SingleParent;
import ix.core.models.ParentReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;

@SingleParent
@Data
@Entity
@Table(name="SRSCID_IMPURITIES_INORGANIC")
public class ImpuritiesInorganic extends ImpuritiesCommonData {

    @Id
    @SequenceGenerator(name = "impInorgSeq", sequenceName = "SRSCID_SQ_IMPURITIES_INORG_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impInorgSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "RELATED_SUBSTANCE_UUID", length=500)
    public String relatedSubstanceUuid;

    @Column(name = "TEST_TYPE", length=500)
    public String testType;

    @Column(name = "LIMIT_VALUE", length=500)
    public String limitValue;

    @Column(name = "LIMIT_TYPE", length=500)
    public String limitType;

    @Column(name = "UNIT", length=500)
    public String unit;

    @Column(name = "COMMENTS", length=4000)
    public String comments;

    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="INORGANIC_TEST_ID")
    public ImpuritiesInorganicTest owner;

    public void setOwner(ImpuritiesInorganicTest impuritiesInorganicTest) {
        this.owner = impuritiesInorganicTest;
    }

}
