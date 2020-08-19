package com.springbootdemo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springbootdemo.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		// @formatter:off
		http.authorizeRequests()
	        	.antMatchers("/",                           //access to anybody
	        				 "/about",
	        				 "/register",
	        				 "/verifyemail",
	        				 "/registrationconfirmed",
	        				 "/invaliduser",
	        				 "/expiredtoken",
        				     "/profilephoto/*",
	        				 "/search").permitAll()
	        	.antMatchers("/css/*",
	        				 "/js/*",
	        				 "/img/*").permitAll()
	        	.antMatchers("/addstatus",                  //access to admins 
	        				 "/viewstatus",
	        				 "/editstatus",
	        				 "/deletestatus")
	        		.hasRole("ADMIN")
        		.antMatchers("/profile",
        					 "/profile/*",
        				     "/editprofileabout",
        				     "/upload-profile-photo",
        				     "/save-interest",
        				     "/delete-interest")               //access to authenticated users
        			.authenticated()
    			.anyRequest()
    			.denyAll()
        	.anyRequest()
	        	.authenticated()
	        	.and()
        	.formLogin()
        		.loginPage("/login")
        		.defaultSuccessUrl("/")
        		.permitAll()
        		.and()
    		.logout()
    			.permitAll();
		
		//@formatter:on
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		// @formatter:off
		auth.inMemoryAuthentication()
			.withUser("john")
			.password("hello")
			.roles("USER");
		
		//@formatter:on
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.userDetailsService(userService)
			.passwordEncoder(passwordEncoder);
		
	}
	
	
}
