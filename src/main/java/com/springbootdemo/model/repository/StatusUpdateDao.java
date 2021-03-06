package com.springbootdemo.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.springbootdemo.model.entity.StatusUpdate;

@Repository
public interface StatusUpdateDao extends PagingAndSortingRepository<StatusUpdate, Long>
{
	StatusUpdate findFirstByOrderByDateAddedDesc();
}
