package com.springbootdemo.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.springbootdemo.model.entity.Interest;
import com.springbootdemo.model.entity.Profile;
import com.springbootdemo.model.entity.SiteUser;
import com.springbootdemo.service.InterestService;
import com.springbootdemo.service.ProfileService;
import com.springbootdemo.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:test.properties")
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
			
			String name = new Random().ints(10,0,10).mapToObj(Integer::toString).collect(Collectors.joining(""));
			user.setEmail(name+"@example.com");
			
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
