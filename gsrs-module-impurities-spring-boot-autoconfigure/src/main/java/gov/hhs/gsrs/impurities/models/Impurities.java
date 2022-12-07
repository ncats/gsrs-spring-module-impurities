package gov.hhs.gsrs.impurities.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import gsrs.BackupEntityProcessorListener;
import gsrs.GsrsEntityProcessorListener;
import gsrs.indexer.IndexerEntityListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import gsrs.ForceUpdateDirtyMakerMixin;
import gsrs.security.GsrsSecurityUtils;
import ix.core.models.*;
import ix.core.SingleParent;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@IndexableRoot
@Backup
@Data
@Entity
@Table(name="SRSCID_IMPURITIES")
public class Impurities extends AbstractGsrsEntity implements ForceUpdateDirtyMakerMixin {

    @Id
    @SequenceGenerator(name = "impSeq", sequenceName = "SRSCID_SQ_IMPURITIES_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "impSeq")
    @Column(name = "ID")
    public Long id;

    @Indexable(facet = true, name = "Source Type", sortable = true)
    @Column(name = "SOURCE_TYPE")
    public String sourceType;

    @Indexable(facet = true, name = "Source", sortable = true)
    @Column(name = "SOURCE")
    public String source;

    @Indexable(sortable = true)
    @Column(name = "SOURCE_ID")
    public String sourceId;

    @Indexable(sortable = true)
    @Column(name = "TYPE")
    public String type;

    @Indexable(sortable = true)
    @Column(name = "SPEC_TYPE")
    public String specType;

    @Indexable(sortable = true)
    @Column(name = "COMPANY_PRODUCT_NAME")
    public String productSubstanceName;

    @Indexable(sortable = true)
    @Column(name = "COMPANY_NAME")
    public String submitterName;

    @Column(name = "PRODUCT_ID")
    public String productId;

    @Column(name = "DATE_TYPE")
    public String dateType;

    @Column(name = "DATE_TYPE_DATE")
    private Date dateTypeDate;

    @Version
    public Long internalVersion;

    @Column(name = "CREATED_BY")
    public String createdBy;

    @Column(name = "MODIFIED_BY")
    public String modifiedBy;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @CreatedDate
    @Indexable(name = "Create Date", sortable = true)
    @Column(name = "CREATE_DATE")
    private Date creationDate;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @LastModifiedDate
    @Indexable(name = "Last Modified Date", sortable = true)
    @Column(name = "MODIFY_DATE")
    private Date lastModifiedDate;

    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    @Indexable(facet=true, name="Deprecated")
    public String getDeprecated() {
        return "Not Deprecated";
    }

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ImpuritiesSubstance> impuritiesSubstanceList = new ArrayList<ImpuritiesSubstance>();

    public void setImpuritiesSubstanceList(List<ImpuritiesSubstance> impuritiesSubstanceList) {
        this.impuritiesSubstanceList = impuritiesSubstanceList;
        if(impuritiesSubstanceList !=null) {
            for (ImpuritiesSubstance imp : impuritiesSubstanceList)
            {
                imp.setOwner(this);
            }
        }
    }

    @Basic(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMPURITIES_TOTAL_ID", referencedColumnName = "ID")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public ImpuritiesTotal impuritiesTotal;

    @PrePersist
    public void prePersist() {
        try {
            UserProfile profile = (UserProfile) GsrsSecurityUtils.getCurrentUser();
            if (profile != null) {
                Principal p = profile.user;
                if (p != null) {
                    this.createdBy = p.username;
                    this.modifiedBy = p.username;
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @PreUpdate
    public void preUpdate() {
        try {
            UserProfile profile = (UserProfile) GsrsSecurityUtils.getCurrentUser();
            if (profile != null) {
                Principal p = profile.user;
                if (p != null) {
                    this.modifiedBy = p.username;
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getCreatedBy () {
        return this.createdBy;
    }

    public Date getCreationDate() {
        //Get from Database
        return this.creationDate;
    }

    public String getModifiedBy () {
        return this.modifiedBy;
    }

    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public String getDateTypeDate() {
        //Convert Date to String, get from database
        return convertDateToString(this.dateTypeDate);
    }

    public void setDateTypeDate(String dateTypeDate) {
        //Convert String to Date, store into database
        this.dateTypeDate = convertStringToDate(dateTypeDate);
    }

    public String convertDateToString(Date dtDate) {

        String strDate = null;
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            if (dtDate != null) {
                strDate = df.format(dtDate);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return strDate;
    }

    public Date convertStringToDate(String strDate) {

        Date dtDate = null;
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            if ((strDate != null) && (strDate.length() > 0)) {
                dtDate = df.parse(strDate);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return dtDate;
    }
}
