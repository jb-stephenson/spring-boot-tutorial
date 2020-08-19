package com.springbootdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootdemo.model.Profile;
import com.springbootdemo.model.ProfileDao;

@Service
public class SearchService {

	@Autowired
	private ProfileDao profileDao;
	
	public List<Profile> search(String text) {
		
		//Interests is the field in the Profile Class
		//Name is the field in the Interest Class
		profileDao.findByInterestsName(text).stream().forEach(System.out::println);
		
		return null;
	}
}
