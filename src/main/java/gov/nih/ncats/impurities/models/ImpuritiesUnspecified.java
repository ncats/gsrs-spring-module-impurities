package gov.nih.ncats.impurities.models;

import gsrs.GsrsEntityProcessorListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import ix.core.models.Indexable;
import ix.core.models.IxModel;
import ix.core.search.text.TextIndexerEntityListener;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import lombok.Data;
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

@Data
@Entity
@Table(name="SRSCID_IMPURITIES_UNSPECIFIED", schema = "srscid")
public class ImpuritiesUnspecified extends AbstractGsrsEntity {

    @Id
    @SequenceGenerator(name = "impUnspecSeq", sequenceName = "SRSCID.SRSCID_SQ_IMPURITIES_UNSPEC_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "impUnspecSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "IMPURITY_TYPE")
    public String impurityType;

    @Column(name = "TEST_TYPE")
    public String testType;

    @Column(name = "LIMIT_VALUE")
    public String limitValue;

    @Column(name = "LIMIT_TYPE")
    public String limitType;

    @Column(name = "UNIT")
    public String unit;

    @Column(name = "COMMENTS")
    public String comments;

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

    /*
    @Indexable(indexed=false)
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="IMPURITIES_TEST_ID")
    public ImpuritiesTesting impuritiesUnspecFromTest;
    */

    @JoinColumn(name = "IMPURITIES_UNSPECIFIED_ID", referencedColumnName = "ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ImpuritiesIdentityCriteria> identityCriteriaList = new ArrayList<ImpuritiesIdentityCriteria>();

}
