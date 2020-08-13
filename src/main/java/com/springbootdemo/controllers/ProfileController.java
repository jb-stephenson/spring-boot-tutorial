package com.springbootdemo.controllers;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.validation.Valid;

import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springbootdemo.exceptions.ImageTooSmallException;
import com.springbootdemo.exceptions.InvalidFileException;
import com.springbootdemo.model.FileInfo;
import com.springbootdemo.model.Interest;
import com.springbootdemo.model.Profile;
import com.springbootdemo.model.SiteUser;
import com.springbootdemo.service.FileService;
import com.springbootdemo.service.InterestService;
import com.springbootdemo.service.ProfileService;
import com.springbootdemo.service.UserService;
import com.springbootdemo.status.PhotoUploadStatus;

@Controller
public class ProfileController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private InterestService interestService;
	
	@Autowired
	private PolicyFactory htmlPolicy;
	
	@Autowired
	FileService fileService;
	
	@Value("${photo.upload.directory}")
	private String photoUploadDirectory;
	
	@Value("${photo.upload.ok}")
	private String photoUploadOk;
	
	@Value("${photo.upload.invalid}")
	private String photoUploadInvalid;
	
	@Value("${photo.upload.ioexception}")
	private String photoUploadIOException;
	
	@Value("${photo.upload.toosmall}")
	private String photoUploadTooSmall;
	
	private SiteUser getUser()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		return userService.get(email);
	}
	
	private ModelAndView showProfile(SiteUser user)
	{
		ModelAndView modelAndView = new ModelAndView();
		if(user == null) {
			modelAndView.setViewName("redirect:/");
			return modelAndView;
		}
		
		Profile profile = profileService.getUserProfile(user);
		if(profile == null) {
			profile = new Profile();
			profile.setUser(user);
			profileService.save(profile);
		}
		
		Profile webProfile = new Profile();
		webProfile.safeCopyFrom(profile);
		
		modelAndView.getModel().put("userId", user.getId());
		modelAndView.getModel().put("profile", profile);
		modelAndView.setViewName("app.profile");
		
		return modelAndView;
	}
	
	@RequestMapping("/profile")
	ModelAndView showProfile()
	{
		SiteUser user = getUser();
		ModelAndView modelAndView = showProfile(user);
		return modelAndView;
	}
	
	@RequestMapping("/profile/{id}")
	ModelAndView showProfile(@PathVariable("id") Long id)
	{
		SiteUser user = userService.get(id);
		ModelAndView modelAndView = showProfile(user);
		return modelAndView;
	}
	
	@RequestMapping(value="/editprofileabout", method=RequestMethod.GET)
	ModelAndView editProfileAbout(ModelAndView modelAndView)
	{
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		Profile webProfile = new Profile();
		webProfile.safeCopyFrom(profile);
		
		modelAndView.setViewName("app.editprofileabout");
		modelAndView.getModel().put("profile", webProfile);
		return modelAndView;
	}
	
	@RequestMapping(value="/editprofileabout", method=RequestMethod.POST)
	ModelAndView editProfileAbout(ModelAndView modelAndView, @Valid Profile webProfile, BindingResult result)
	{
		modelAndView.setViewName("app.editprofileabout");
	
		SiteUser user = getUser();
		
		Profile profile = profileService.getUserProfile(user);
		profile.safeMergeFrom(webProfile, htmlPolicy);
		
		if(!result.hasErrors())
		{
			profileService.save(profile);
			modelAndView.setViewName("redirect:/profile");
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value="/upload-profile-photo", method=RequestMethod.POST)
	@ResponseBody // Return data in JSON format
	public ResponseEntity<PhotoUploadStatus> handlePhotoUpload(@RequestParam("file") MultipartFile file)
	{
		FileInfo photoInfo;
		
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		Path oldPhotoPath = profile.getPhoto(photoUploadDirectory);
		
		PhotoUploadStatus photoStatus = new PhotoUploadStatus(photoUploadOk);
		
		try {
			photoInfo = fileService.saveImageFile(file, photoUploadDirectory, "photo", "p" + user.getId(), 100, 100);
			profile.setPhotoDetails(photoInfo);
			profileService.save(profile);
			
			if(oldPhotoPath != null)
				Files.delete(oldPhotoPath);
		} catch (InvalidFileException e) {
			photoStatus.setMessage(photoUploadInvalid);
			e.printStackTrace();
		} catch (IOException e) {
			photoStatus.setMessage(photoUploadIOException);
			e.printStackTrace();
		} catch (ImageTooSmallException e) {
			photoStatus.setMessage(photoUploadTooSmall);
			e.printStackTrace();
		}
		
		
		
		return new ResponseEntity<PhotoUploadStatus>(photoStatus, HttpStatus.OK);
	}
	
	@RequestMapping(value="/profilephoto/{id}", method=RequestMethod.GET)
	@ResponseBody
	ResponseEntity<InputStreamResource> servePhoto(@PathVariable("id") Long id) throws IOException
	{
		SiteUser user = userService.get(id);
		Profile profile = profileService.getUserProfile(user);
		
		Path photoPath = Paths.get(photoUploadDirectory, "default", "tyranitar.jpg");
		if((photoPath != null) && (profile.getPhoto(photoUploadDirectory) != null))
		{
			photoPath = profile.getPhoto(photoUploadDirectory);
		}
		
		return ResponseEntity
				.ok()
				.contentLength(Files.size(photoPath))
				.contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(photoPath.toString())))
				.body(new InputStreamResource(Files.newInputStream(photoPath, StandardOpenOption.READ)));
	}
	
	@RequestMapping(value="/save-interest", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveInterest(@RequestParam(name="name") String interestName) {
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		String cleanedInterestName = htmlPolicy.sanitize(interestName);
		
		Interest interest = interestService.createIfNotExisteds(cleanedInterestName);
		
		profile.addInterest(interest);
		profileService.save(profile);
		
		return new ResponseEntity(null, HttpStatus.OK);
	}
	
	@RequestMapping(value="/delete-interest", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> deleteInterest(@RequestParam(name="name") String interestName) {
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		profile.removeInterest(interestName);
		
		profileService.save(profile);
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
