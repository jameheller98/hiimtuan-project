package com.hiimtuan.user_service.repository;

import com.hiimtuan.common_service.constant.RoleEnum;
import com.hiimtuan.user_service.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}