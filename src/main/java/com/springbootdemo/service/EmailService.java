package com.springbootdemo.service;

import java.util.Date;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@EnableAsync
@Service
public class EmailService {

	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${mail.enable}")
	private Boolean enable;
	
	@Value("${site.url}")
	private String url;
	
	public void send(MimeMessagePreparator preparator)
	{
		if(enable) mailSender.send(preparator);
	}
	
	@Autowired
	public EmailService(TemplateEngine templateEngine)
	{
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		
		templateResolver.setPrefix("mail/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCacheable(false);
		templateEngine.setTemplateResolver(templateResolver);
		
		this.templateEngine = templateEngine;
	}
	
	@Async
	public void sendVerificationEmail(String emailAddress, String token) 
	{
		Context context = new Context();
		context.setVariable("token", token);
		context.setVariable("url", url);
		
		String emailContents = templateEngine.process("verifyemail", context);
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() 
		{
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception 
			{
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage); 
				
				message.setTo(emailAddress);
				message.setFrom(new InternetAddress("no-reply@springbootdemo.com"));
				message.setSubject("Please Verify Your Email Address");
				message.setSentDate(new Date());
				
				message.setText(emailContents, true);
			}
		};
		
		send(preparator);
	}
}
