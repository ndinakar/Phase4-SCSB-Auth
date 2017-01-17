package org.recap.repository;

import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmendrag on 17/1/17.
 */
public class UserDetailsRepositoryUT extends BaseTestCase {

    @Autowired
    public UserDetailsRepository userRepo;

    @Test
    public void findUserByEmailId(){
        String emailId="john@cul.com";
        InstitutionEntity institutionEntity=new InstitutionEntity();
        institutionEntity.setInstitutionId(2);

        UsersEntity usersEntity=userRepo.findByEmailIdAndInstitutionEntity(emailId,institutionEntity);

        assertEquals(usersEntity.getLoginId(),"jhon");
    }


}
