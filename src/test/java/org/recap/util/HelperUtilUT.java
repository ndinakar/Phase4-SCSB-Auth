package org.recap.util;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.repository.jpa.InstitutionDetailsRepository;


import java.util.Collections;

import static org.junit.Assert.assertNull;

public class HelperUtilUT extends BaseTestCase {

    @Mock
    public InstitutionDetailsRepository institutionDetailsRepository;
    @Test
    public void getInstitutionIdByCodeNullCheck(){
        InstitutionEntity institutionEntity = new InstitutionEntity();
        String value = null;
        HelperUtil helperUtil = new HelperUtil();
        InstitutionEntity institutionEntity1 = helperUtil.getInstitutionIdByCode(value);
        assertNull(institutionEntity1);
    }
}
