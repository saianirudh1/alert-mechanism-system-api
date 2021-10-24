package com.alertmechanismapi.alertmechanismsystemapi.repo;

import com.alertmechanismapi.alertmechanismsystemapi.dao.Team;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
}