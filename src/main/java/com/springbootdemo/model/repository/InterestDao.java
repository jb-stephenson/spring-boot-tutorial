package com.springbootdemo.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springbootdemo.model.entity.Interest;

@Repository
public interface InterestDao extends CrudRepository<Interest, Long>{
	Interest findOneByName(String name);
}
