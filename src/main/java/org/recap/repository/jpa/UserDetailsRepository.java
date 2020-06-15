package org.recap.repository.jpa;

import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.UsersEntity;

/**
 * Created by dharmendrag on 29/11/16.
 */
public interface UserDetailsRepository extends BaseRepository<UsersEntity> {

    /**
     * Gets users entity for the given login id.
     *
     * @param loginId the login id
     * @return the users entity
     */
    UsersEntity findByLoginId(String loginId);

    /**
     * Gets users entity for the given login id and  institution entity.
     *
     * @param loginId       the login id
     * @param institutionId the institution id
     * @return the users entity
     */
    UsersEntity findByLoginIdAndInstitutionEntity(String loginId, InstitutionEntity institutionId);

}
