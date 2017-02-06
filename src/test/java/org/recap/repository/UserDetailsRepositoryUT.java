package org.recap.repository;

import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmendrag on 17/1/17.
 */
public class UserDetailsRepositoryUT extends BaseTestCase {

    @Autowired
    public UserDetailsRepository userRepo;

    @Autowired
    public InstitutionDetailsRepository institutionDetailsRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void createUser(){
        UsersEntity usersEntity=new UsersEntity();
        usersEntity.setLoginId("julius");
        usersEntity.setEmailId("julius@example.org");
        InstitutionEntity newInstitution=getInstitution();
        usersEntity.setUserDescription("test User");
        usersEntity.setInstitutionId(newInstitution.getInstitutionId());
        usersEntity.setCreatedBy("superadmin");
        usersEntity.setCreatedDate(new Date());
        usersEntity.setLastUpdatedBy("superadmin");
        usersEntity.setLastUpdatedDate(new Date());

        UsersEntity savedUser=userRepo.saveAndFlush(usersEntity);
        entityManager.refresh(savedUser);

        assertEquals(usersEntity.getLoginId(),savedUser.getLoginId());

        UsersEntity byLoginId=userRepo.findByLoginId("julius");
        assertEquals("julius",byLoginId.getLoginId());
        assertEquals("test User",byLoginId.getUserDescription());
        assertEquals("julius@example.org",byLoginId.getEmailId());

    }

    private InstitutionEntity getInstitution(){
        InstitutionEntity institutionEntity=new InstitutionEntity();
        institutionEntity.setInstitutionCode("CAM");
        institutionEntity.setInstitutionName("Cambridge");
        InstitutionEntity savedInstitution=institutionDetailsRepository.saveAndFlush(institutionEntity);
        entityManager.refresh(savedInstitution);
        return savedInstitution;
    }


}
