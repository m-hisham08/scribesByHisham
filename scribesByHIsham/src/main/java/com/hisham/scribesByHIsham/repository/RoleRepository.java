package com.hisham.scribesByHIsham.repository;

import com.hisham.scribesByHIsham.model.Role;
import com.hisham.scribesByHIsham.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
