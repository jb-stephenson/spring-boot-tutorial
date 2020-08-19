package com.springbootdemo.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.springbootdemo.model.entity.Profile;
import com.springbootdemo.model.entity.SiteUser;

public interface ProfileDao extends CrudRepository<Profile, Long>
{
	Profile findByUser(SiteUser user);

	List<Profile> findByInterestsNameContainingIgnoreCase(String text);
}
