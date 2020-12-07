package org.recap.IT.repository;

import org.junit.Test;
import org.recap.IT.BaseTestCase;
import org.recap.controller.LoginController;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by pvsubrah on 6/22/16.
 */
public class InstitutionDetailsRepositoryIT extends BaseTestCase {

    private static final Logger logger = LoggerFactory.getLogger(InstitutionDetailsRepositoryIT.class);
    @Autowired
    InstitutionDetailsRepository institutionDetailsRepository;

    @Test
    public void saveAndFind() throws Exception {
        assertNotNull(institutionDetailsRepository);

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setInstitutionCode("OXF");
        institutionEntity.setInstitutionName("Oxford");
try {
    InstitutionEntity savedInstitutionEntity = institutionDetailsRepository.save(institutionEntity);
    assertNotNull(savedInstitutionEntity);
    assertNotNull(savedInstitutionEntity.getId());
    assertEquals("OXF", savedInstitutionEntity.getInstitutionCode());
    assertEquals("Oxford", savedInstitutionEntity.getInstitutionName());
}catch (Exception e){
    logger.error("Test Issue");
    e.printStackTrace();
}

        InstitutionEntity byInstitutionCode = institutionDetailsRepository.findByInstitutionCode("OXF");
        assertNotNull(byInstitutionCode);
        assertEquals("OXF",byInstitutionCode.getInstitutionCode());
        assertEquals("Oxford",byInstitutionCode.getInstitutionName());

        InstitutionEntity byInstitutionName = institutionDetailsRepository.findByInstitutionName("Oxford");
        assertNotNull(byInstitutionName);
    }

}