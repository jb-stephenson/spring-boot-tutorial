package com.springbootdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.springbootdemo.model.Profile;
import com.springbootdemo.model.ProfileDao;
import com.springbootdemo.model.SiteUser;

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
