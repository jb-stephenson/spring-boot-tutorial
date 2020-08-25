package com.springbootdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootdemo.model.entity.Profile;
import com.springbootdemo.model.entity.SiteUser;
import com.springbootdemo.model.repository.ProfileDao;

@Service
public class ProfileService 
{
	@Autowired
	private ProfileDao profileDao;
	
	public void save(Profile profile)
	{
		profileDao.save(profile);
	}
	
	public Profile getUserProfile(SiteUser user)
	{
		return profileDao.findByUser(user);
	}
}
