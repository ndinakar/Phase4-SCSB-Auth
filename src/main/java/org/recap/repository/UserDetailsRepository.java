package org.recap.repository;

import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dharmendrag on 29/11/16.
 */
public interface UserDetailsRepository extends JpaRepository<UsersEntity,Integer> {

    UsersEntity findByLoginId(String loginId);

    UsersEntity findByLoginIdAndInstitutionEntity(String loginId, InstitutionEntity institutionId);

    UsersEntity findByUserId(Integer userId);




}
