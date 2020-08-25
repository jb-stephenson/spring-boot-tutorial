package com.springbootdemo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.springbootdemo.model.entity.StatusUpdate;
import com.springbootdemo.model.repository.StatusUpdateDao;

@Component
public class StatusUpdateService 
{
	@Value("${status.pagesize}")
	private int pageSize;
	
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
		PageRequest request = PageRequest.of(pageNumber-1, pageSize, Sort.Direction.DESC, "dateAdded");
		
		return statusUpdateDao.findAll(request);
	}

	public void delete(Long id) 
	{
		statusUpdateDao.deleteById(id);
	}

	public Optional<StatusUpdate> get(Long id) 
	{
		return statusUpdateDao.findById(id);
	}
}
