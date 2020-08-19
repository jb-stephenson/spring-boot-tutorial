package com.springbootdemo.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import com.springbootdemo.App;
import com.springbootdemo.model.entity.StatusUpdate;
import com.springbootdemo.model.repository.StatusUpdateDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
@WebAppConfiguration
@Transactional
public class StatusTest 
{
	@Autowired
	private StatusUpdateDao statusUpdateDao;
	
	@Test
	public void testSave()
	{
		StatusUpdate status= new StatusUpdate("Test Status Update");
		
		statusUpdateDao.save(status);
		
		assertNotNull("Non-null Id", status.getId());
		assertNotNull("Non-null Date", status.getDateAdded());
		
		StatusUpdate retrieved = statusUpdateDao.findOne(status.getId());
		
		assertEquals("Matching StatusUpdate", status, retrieved);
	}
	
	@Test
	public void testFindLatest() 
	{
		Calendar calendar = Calendar.getInstance();
		StatusUpdate lastStatusUpdate = null;
		
		for(int i=0; i<10; i++)
		{
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			
			StatusUpdate status = new StatusUpdate("Status Update " + i, calendar.getTime());
			
			statusUpdateDao.save(status);
			
			lastStatusUpdate = status;
		}
		
		StatusUpdate retrieved = statusUpdateDao.findFirstByOrderByDateAddedDesc();
		
		assertEquals("Latest Status Update", lastStatusUpdate, retrieved);
	}
}
