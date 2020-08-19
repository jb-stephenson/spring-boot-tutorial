package com.springbootdemo.model.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springbootdemo.model.entity.SiteUser;

@Repository
public interface UserServiceDao extends CrudRepository<SiteUser, Long>{
	SiteUser findByEmail(String email);
}
