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
@Table(name="SRSCID_IMPURITIES_SUBSTANCE", schema = "srscid")
public class ImpuritiesSubstance extends AbstractGsrsEntity {

    @Id
    @SequenceGenerator(name = "impSubSeq", sequenceName = "SRSCID.SRSCID_SQ_IMPURITIES_SUBS_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "impSubSeq")
    @Column(name = "ID")
    public Long id;

    @Indexable
    @Column(name = "SUBSTANCE_UUID")
    public String substanceUuid;

    @Column(name="LOW")
    public Double low;

    @Column(name="HIGH")
    public Double high;

    @Column(name="UNIT")
    public String unit;

    @Column(name="COMMENTS")
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

    @JoinColumn(name = "IMPURITIES_SUBSTANCE_ID", referencedColumnName = "ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ImpuritiesTesting> impuritiesTestList = new ArrayList<ImpuritiesTesting>();

    @JoinColumn(name = "IMPURITIES_SUBSTANCE_ID", referencedColumnName = "ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ImpuritiesResidualSolvents> impuritiesResidualSolventsList = new ArrayList<ImpuritiesResidualSolvents>();

    @JoinColumn(name = "IMPURITIES_SUBSTANCE_ID", referencedColumnName = "ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ImpuritiesInorganic> impuritiesInorganicList = new ArrayList<ImpuritiesInorganic>();

}
