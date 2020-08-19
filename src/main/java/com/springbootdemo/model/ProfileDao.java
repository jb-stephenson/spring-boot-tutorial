package com.springbootdemo.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProfileDao extends CrudRepository<Profile, Long>
{
	Profile findByUser(SiteUser user);

	List<Profile> findByInterestsName(String text);
}
