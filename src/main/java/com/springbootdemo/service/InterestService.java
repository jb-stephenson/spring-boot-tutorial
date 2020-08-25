package com.springbootdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootdemo.model.entity.Interest;
import com.springbootdemo.model.repository.InterestDao;

@Service
public class InterestService {

	@Autowired
	private InterestDao interestDao;
	
	public Long count()
	{
		return interestDao.count();
	}
	
	public Interest get(String interestName)
	{
		return interestDao.findOneByName(interestName);
	}
	
	public void save(Interest interest)
	{
		interestDao.save(interest);
	}
	
	public Interest createIfNotExisteds(String interestText)
	{
		Interest interest = interestDao.findOneByName(interestText);
		
		if(interest == null) {
			interest = new Interest(interestText);
			interestDao.save(interest);
		}
		
		return interest;
	}
}
