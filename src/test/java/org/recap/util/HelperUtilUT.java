package org.recap.util;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

public class HelperUtilUT extends BaseTestCase {
    @Mock
    HelperUtil helperUtil;
    @Mock
    public InstitutionDetailsRepository institutionDetailsRepository;
    @Test
    public void getInstitutionIdByCode(){
        InstitutionEntity institutionEntity = new InstitutionEntity();
        String value = "3";
        Mockito.when(institutionDetailsRepository.findByInstitutionCode(value)).thenReturn(institutionEntity);
        Mockito.doCallRealMethod().when(helperUtil).getInstitutionIdByCode(value);
     //   InstitutionEntity institutionEntity1 = helperUtil.getInstitutionIdByCode(value);
      //  assertNotNull(institutionEntity1);
    }
}
