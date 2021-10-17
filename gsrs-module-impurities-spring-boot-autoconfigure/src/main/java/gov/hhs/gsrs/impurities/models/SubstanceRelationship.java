package gov.hhs.gsrs.impurities.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import gsrs.BackupEntityProcessorListener;
import gsrs.GsrsEntityProcessorListener;
import gsrs.indexer.IndexerEntityListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import gsrs.security.GsrsSecurityUtils;
import ix.core.models.*;
import ix.core.search.text.TextIndexerEntityListener;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import lombok.Data;
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
@Table(name="SRSCID_RELATIONSHIP_V")
public class SubstanceRelationship extends AbstractGsrsEntity {

    @Id
    @Column(name="UUID")
    public String id;

    @Column(name="SUBSTANCE_ID")
    public String substanceId;

    @Column(name="OWNER_SUBSTANCE_KEY")
    public String ownerBdnum;

    @Column(name="TYPE")
    public String relationshipType;

    @Column(name="REF_UUID")
    public String relationshipUuid;

    @Column(name="REF_PNAME")
    public String relationshipName;

    @Column(name="APPROVAL_ID")
    public String relationshipUnii;

    public SubstanceRelationship () {}

}
