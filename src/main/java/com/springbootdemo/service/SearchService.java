package com.springbootdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.springbootdemo.model.dto.SearchResult;
import com.springbootdemo.model.entity.Profile;
import com.springbootdemo.model.repository.ProfileDao;

@Service
public class SearchService {

	@Value("${results.pagesize}")
	private int pageSize;
	
	@Autowired
	private ProfileDao profileDao;
	
	public Page<SearchResult> search(String text, int pageNumber) {
		
		PageRequest request = new PageRequest(pageNumber-1, pageSize);
		
		//Interests is the field in the Profile Class
		//Name is the field in the Interest Class
		Page<Profile> results = profileDao.findByInterestsNameContainingIgnoreCase(text, request);
		
		Converter<Profile, SearchResult> converter = new Converter<Profile, SearchResult>() {

			public SearchResult convert(Profile profile) {
				return new SearchResult(profile);
			}
			
		};
		
		return results.map(converter);
	}
}
