package gov.nih.ncats.impurities.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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
/*
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
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
 */
import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name="SRSCID_IMPURITIES", schema = "srscid")
public class Impurities extends AbstractGsrsEntity {

    @Id
    @SequenceGenerator(name = "impSeq", sequenceName = "SRSCID.SRSCID_SQ_IMPURITIES_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "impSeq")
    @Column(name = "ID")
    public Long id;

    @Indexable(facet = true, name = "Source Type")
    @Column(name = "SOURCE_TYPE")
    public String sourceType;

    @Indexable(facet = true, name = "Source")
    @Column(name = "SOURCE")
    public String source;

    @Column(name = "SOURCE_ID")
    public String sourceId;

    @Column(name = "TYPE")
    public String type;

    @Column(name = "SPEC_TYPE")
    public String specType;

    @Column(name = "COMPANY_PRODUCT_NAME")
    public String companyProductName;

    @Column(name = "COMPANY_NAME")
    public String companyName;

    @Column(name = "PRODUCT_ID")
    public String productId;

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

    @JoinColumn(name = "IMPURITIES_ID", referencedColumnName = "ID")
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    public List<ImpuritiesSubstance> impuritiesSubstanceList = new ArrayList<ImpuritiesSubstance>();

    @Basic(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMPURITIES_TOTAL_ID", referencedColumnName = "ID")
    @OneToOne(cascade = CascadeType.ALL)
    public ImpuritiesTotal impuritiesTotal;

    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    @Indexable(facet=true, name="Deprecated")
    public String getDeprecated(){
        return "Not Deprecated";
    }

    @Transient
    @JsonProperty("_approvalID")
    public String _approvalID;

    @Transient
    @JsonProperty("_name")
    public String _name;
}
