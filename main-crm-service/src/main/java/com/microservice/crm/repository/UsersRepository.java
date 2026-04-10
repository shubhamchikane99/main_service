package com.microservice.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.crm.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

}
