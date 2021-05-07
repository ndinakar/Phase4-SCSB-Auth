package org.recap.IT.util;

import org.junit.Test;
import org.mockito.Mock;
import org.recap.IT.BaseTestCase;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.recap.util.HelperUtil;


import static org.junit.Assert.assertNull;

public class HelperUtilIT extends BaseTestCase {

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
