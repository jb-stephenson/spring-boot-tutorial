package com.springbootdemo.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusUpdateDao extends PagingAndSortingRepository<StatusUpdate, Long>
{
	StatusUpdate findFirstByOrderByDateAddedDesc();
}
