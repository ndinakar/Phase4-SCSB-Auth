package org.recap.repository;

import org.recap.model.jpa.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by dharmendrag on 13/12/16.
 */
public interface RolesDetailsRepositorty extends JpaRepository<RoleEntity, Integer> {

    Page<RoleEntity> findByRoleName(String roleName, Pageable pageable);


}
