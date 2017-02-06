package org.recap.repository;

import org.recap.model.jpa.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hemalathas on 22/6/16.
 */

public interface InstitutionDetailsRepository extends JpaRepository<InstitutionEntity,Integer> {
    InstitutionEntity findByInstitutionId(Integer institutionId);
    InstitutionEntity findByInstitutionCode(String institutionCode);
    InstitutionEntity findByInstitutionName(String institutionName);
}
