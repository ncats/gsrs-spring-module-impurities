package gov.hhs.gsrs.impurities.models;

import gsrs.model.AbstractGsrsEntity;
import ix.core.models.*;
import lombok.Data;
import jakarta.persistence.*;

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

    @Column(name="REF_PNAME", length=1000)
    public String relationshipName;

    @Column(name="APPROVAL_ID")
    public String relationshipUnii;

    public SubstanceRelationship () {}

}
