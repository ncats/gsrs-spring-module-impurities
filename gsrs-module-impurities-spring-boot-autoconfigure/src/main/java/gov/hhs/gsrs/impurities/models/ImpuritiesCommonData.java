package gov.hhs.gsrs.impurities.models;

// GSRS Imports
import gsrs.model.AbstractGsrsEntity;
import gsrs.security.GsrsSecurityUtils;
import gsrs.ForceUpdateDirtyMakerMixin;
import ix.core.models.Indexable;
import ix.core.models.Principal;
import ix.core.models.UserProfile;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;

// Jackson databind
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

// Jakarta
import jakarta.persistence.*;

// lombok
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
@Slf4j
public class ImpuritiesCommonData extends AbstractGsrsEntity implements ForceUpdateDirtyMakerMixin{

    @Indexable(facet = true, name = "Record Created By")
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Indexable(facet = true, name = "Record Last Edited By")
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @CreatedDate
    @Indexable(facet = true, name = "Record Create Date", sortable=true)
    @Column(name = "CREATE_DATE")
    private Date creationDate;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @LastModifiedDate
    @Indexable(facet = true, name = "Record Last Edited", sortable=true)
    @Column(name = "MODIFY_DATE")
    private Date lastModifiedDate;

    @Version
    @Column(name = "INTERNAL_VERSION")
    public Long internalVersion;

    public ImpuritiesCommonData () {
    }

    public String getCreatedBy () {
        //Get from Database
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

    /*
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
    */
}
