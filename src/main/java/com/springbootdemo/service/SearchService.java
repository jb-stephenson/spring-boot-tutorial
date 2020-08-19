package com.springbootdemo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootdemo.model.dto.SearchResult;
import com.springbootdemo.model.entity.Profile;
import com.springbootdemo.model.repository.ProfileDao;

@Service
public class SearchService {

	@Autowired
	private ProfileDao profileDao;
	
	public List<SearchResult> search(String text) {
		
		//Interests is the field in the Profile Class
		//Name is the field in the Interest Class
		return profileDao.findByInterestsNameContainingIgnoreCase(text).stream().map(SearchResult::new).collect(Collectors.toList());
	}
}
