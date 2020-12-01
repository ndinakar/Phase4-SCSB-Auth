package org.recap.UT.util;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.UT.BaseTestCaseUT;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.recap.util.HelperUtil;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class HelperUtilUT extends BaseTestCaseUT {

    @InjectMocks
    HelperUtil helperUtil;
    @Mock
    public InstitutionDetailsRepository institutionDetailsRepository;

    @Test
    public void getInstitutionIdByCode() {
        InstitutionEntity institutionEntity = getInstitutionEntity();
        String value = "test";
        Mockito.when(institutionDetailsRepository.findByInstitutionCode(value)).thenReturn(institutionEntity);
        InstitutionEntity institutionEntity1 = helperUtil.getInstitutionIdByCode(value);
        assertNotNull(institutionEntity1);
    }

    @Test
    public void getInstitutionIdByCodeNullCheck() {
        String value = "test";
        InstitutionEntity institutionEntity = helperUtil.getInstitutionIdByCode(value);
        assertNull(institutionEntity);
    }

    private InstitutionEntity getInstitutionEntity() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(1);
        institutionEntity.setInstitutionName("PUL");
        institutionEntity.setInstitutionCode("PUL");
        return institutionEntity;
    }
}
