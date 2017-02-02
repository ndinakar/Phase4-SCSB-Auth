package org.recap.repository;

import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by hemalathas on 22/12/16.
 */
public class RolesDetailsRepositortyUT extends BaseTestCase {

    @Autowired
    RolesDetailsRepositorty rolesDetailsRepositorty;

    @Autowired
    PermissionsRepository permissionsRepository;

    @PersistenceContext
    EntityManager entityManager;



   @Test
    public void createRole() {
       RoleEntity roleEntity = new RoleEntity();
       roleEntity.setRoleName("patron");
       roleEntity.setRoleDescription("patron from CUL");
       roleEntity.setCreatedDate(new Date());
       roleEntity.setCreatedBy("superadmin");
       roleEntity.setLastUpdatedDate(new Date());
       roleEntity.setLastUpdatedBy("superadmin");


       PermissionEntity permissionEntity = getPermissionEntity();


       Set<PermissionEntity> permissionEntities = new HashSet<>();
       permissionEntities.add(permissionEntity);
       roleEntity.setPermissions(permissionEntities);

       RoleEntity savedRoleEntity = rolesDetailsRepositorty.save(roleEntity);
       assertEquals(roleEntity.getRoleName(),savedRoleEntity.getRoleName());
       assertEquals(roleEntity.getRoleDescription(),savedRoleEntity.getRoleDescription());
   }


    public PermissionEntity getPermissionEntity(){
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setPermissionDesc("Permission to create Request");
        permissionEntity.setPermissionId(1);
        permissionEntity.setPermissionName("CreateRequest");
        PermissionEntity savedPermission = permissionsRepository.saveAndFlush(permissionEntity);
        entityManager.refresh(savedPermission);
        return savedPermission;
    }



}