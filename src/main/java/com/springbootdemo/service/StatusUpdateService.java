package com.springbootdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.springbootdemo.model.StatusUpdate;
import com.springbootdemo.model.StatusUpdateDao;

@Component
public class StatusUpdateService 
{
	private final static int PAGESIZE = 5;
	
	@Autowired
	private StatusUpdateDao statusUpdateDao;
	
	public void save(StatusUpdate statusUpdate)
	{
		statusUpdateDao.save(statusUpdate);
	}
	
	public StatusUpdate getLatest()
	{
		return statusUpdateDao.findFirstByOrderByDateAddedDesc();
	}

	public Page<StatusUpdate> getPage(int pageNumber) 
	{
		PageRequest request = new PageRequest(pageNumber-1, PAGESIZE, Sort.Direction.DESC, "dateAdded");
		
		return statusUpdateDao.findAll(request);
	}

	public void delete(Long id) 
	{
		statusUpdateDao.delete(id);
	}

	public StatusUpdate get(Long id) 
	{
		return statusUpdateDao.findOne(id);
	}
}
