package org.recap.util;

import org.recap.model.jpa.InstitutionEntity;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sheiks on 03/03/17.
 */
@Component
public class HelperUtil {

    @Autowired
    private InstitutionDetailsRepository institutionDetailsRepository;

    /**
     * To get institution entity for the given institution code.
     *
     * @param value the value
     * @return the institution id by code
     */
    public InstitutionEntity getInstitutionIdByCode(String value) {
        if (value != null) {
            InstitutionEntity institutionEntity = institutionDetailsRepository.findByInstitutionCode(value);
            if (null != institutionEntity) {
                return institutionEntity;
            }
        }
        return null;
    }
}
