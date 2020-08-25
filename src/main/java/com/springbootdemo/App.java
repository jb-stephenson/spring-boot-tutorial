package com.springbootdemo;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

@EnableAsync
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class App extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer {

	public static void main(String[] args) 
	{	
		//Takes class name and string arguments
		SpringApplication.run(App.class, args);
	}
	
	//Allows us to deploy as a traditional war file
	@Override
	public SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		return application.sources(App.class);
	}
	
	@Bean
	public TilesConfigurer tilesConfigurer()
	{
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		
		String[] defs = {"/WEB-INF/tiles.xml"};
		
		tilesConfigurer.setDefinitions(defs);
		
		return tilesConfigurer;
	}
	
	@Bean
	public UrlBasedViewResolver tilesViewResolver()
	{
		UrlBasedViewResolver tilesViewResolver = new UrlBasedViewResolver();
		
		tilesViewResolver.setViewClass(TilesView.class);
		
		return tilesViewResolver;
	}
	
	@Bean
	public PasswordEncoder getEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Configuration
	public class ServerConfig {
		
		@Bean
		public ConfigurableServletWebServerFactory webServerFactory() {
			TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
			factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/403"));
			return factory;
		}
	}
	
	@Bean
	PolicyFactory getUserHtmlPolicy()
	{
		return new HtmlPolicyBuilder()
					.allowCommonBlockElements()
					.allowCommonInlineFormattingElements()
					.toFactory();
	}
}
