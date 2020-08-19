package com.springbootdemo.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.springbootdemo.App;
import com.springbootdemo.model.Interest;
import com.springbootdemo.model.Profile;
import com.springbootdemo.model.SiteUser;
import com.springbootdemo.service.InterestService;
import com.springbootdemo.service.ProfileService;
import com.springbootdemo.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
@WebAppConfiguration
@Transactional
public class ProfileTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private InterestService interestService;
	
	private SiteUser[] users = {
			new SiteUser("sean@gmail.com", "seancleric", "Sean", "Cleric"),
			new SiteUser("gab@gmail.com", "gabriel", "Gabe", "Wrestler"),
			new SiteUser("taylor@gmail.com", "taylor", "Taylor", "Bard")
	};
	
	private String[][] interests = {
			{"music", "guitar_xxxx", "plants"},
			{"music", "music", "philosophy"},
			{"philosophy_lklklk", "football"}
	};
	
	@Test
	public void testInterests() {
		
		for(int i=0; i < users.length; i++)
		{
			SiteUser user = users[i];
			String[] interestArray = interests[i];
			
			userService.register(user);
			
			HashSet<Interest> interestSet = new HashSet<>();
			
			for(String interestText : interestArray) {
				Interest interest = interestService.createIfNotExisteds(interestText);
				interestSet.add(interest);
				
				assertNotNull("Interest should not be null", interest);
				assertNotNull("Interest should have ID", interest.getId());
				assertEquals("Text should match", interestText, interest.getName());
			}
			
			Profile profile = new Profile(user);
			profile.setInterests(interestSet);
			profileService.save(profile);
			
			Profile retrievedProfile = profileService.getUserProfile(user);
			
			assertEquals("Interests should match", interestSet, retrievedProfile.getInterests());
		}
	}
}
