package com.springbootdemo.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
public class ProfileControllerRestTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private InterestService interestService;
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webAppContext;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	}
	
	@Test
	@WithMockUser(username="john@gmail.com")
	public void testSaveAndDeleteInterest() throws Exception {
		String interestText = "some_interest_here";
		
		mockMvc.perform(post("/save-interest").param("name", interestText)).
			andExpect(status().isOk());
		
		Interest interest = interestService.get(interestText);
		
		assertNotNull("Interest should exist", interest);
		assertEquals("Retrieved interest should match", interestText, interest.getName());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		SiteUser user = userService.get(email);
		Profile profile = profileService.getUserProfile(user);
		
		assertTrue("Profile should contain interest", profile.getInterests().contains(new Interest(interestText)));
		
		mockMvc.perform(post("/delete-interest").param("name", interestText)).
			andExpect(status().isOk());
		
		profile = profileService.getUserProfile(user);
		
		assertFalse("Profile should not contain interest", profile.getInterests().contains(new Interest(interestText)));
	}
}
