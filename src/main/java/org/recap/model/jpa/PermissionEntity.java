package org.recap.model.jpa;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dharmendrag on 29/11/16.
 */
@Cacheable(true)
@Entity
@Table(name="permissions_t",schema="recap",catalog="")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "permission_id"))
public class PermissionEntity extends AbstractEntity<Integer> {
    @Column(name="permission_name")
    private String permissionName;

    @Column(name="permission_description")
    private String permissionDesc;
}
