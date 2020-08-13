package com.springbootdemo.model;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserServiceDao extends CrudRepository<SiteUser, Long>{
	SiteUser findByEmail(String email);
}
