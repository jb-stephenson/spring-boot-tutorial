package com.springbootdemo.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
//@Transactional
public class BulkTest {

	private static final String namesFile = "/com/springbootdemo/tests/data/names.txt";
	private static final String hobbiesFile = "/com/springbootdemo/tests/data/hobbies.txt";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private InterestService interestService;
	
	private List<String> loadFile(String filename, int maxLength) throws IOException {
		Path filePath = new ClassPathResource(filename).getFile().toPath();
		
		Stream<String> stream = Files.lines(filePath);
		
		// @formatter:off
		
		List<String> items = stream
			.filter(line -> !line.isEmpty())
			.map(line -> line.trim())
			.filter(line -> line.length() <= maxLength)
			.map(line -> line.substring(0,1).toUpperCase() + line.substring(1).toLowerCase())
			.collect(Collectors.toList());
		
		// @formatter:on
		stream.close();
		
		return items;
	}
	
	@Ignore
	@Test
	public void createTestData() throws IOException {
		Random random = new Random();
		
		List<String> interests = loadFile(hobbiesFile, 25);
		List<String> names = loadFile(namesFile, 25);
		
		for(int i=0; i < 4000; i++) {
			String firstname = names.get(random.nextInt(names.size()));
			String surname = names.get(random.nextInt(names.size()));
			
			String email = firstname.toLowerCase() + surname.toLowerCase() + "@example.com";
			
			if(userService.get(email) != null) {
				continue;
			}
			
			String password = "pass" + firstname.toLowerCase();
			password  = password.substring(0, Math.min(15, password.length()));
			
			assertTrue(password.length() <= 15);
			
			SiteUser user = new SiteUser(email, password, firstname, surname);
			user.setEnabled(random.nextInt(5) != 0);
			userService.register(user);
			
			Profile profile = new Profile(user);
			int numInterests = random.nextInt(7);
			Set<Interest> userInterests = new HashSet<Interest>();
			
			for(int j=0; j < numInterests; j++) {
				String interestText = interests.get(random.nextInt(interests.size()));
				
				Interest interest = interestService.createIfNotExisteds(interestText);
				userInterests.add(interest);
			}
			
			profile.setInterests(userInterests);
			profileService.save(profile);
		}
	}
}
