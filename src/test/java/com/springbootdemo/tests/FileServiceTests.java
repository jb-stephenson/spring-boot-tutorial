package com.springbootdemo.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.springbootdemo.App;
import com.springbootdemo.service.FileService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
@WebAppConfiguration
@Transactional
public class FileServiceTests {

	@Autowired
	FileService fileService;
	
	@Value("${photo.upload.directory}")
	private String photoUploadDirectory;
	
	@Test
	public void testGetFileExtension() throws Exception
	{
		Method method = fileService.getClass().getDeclaredMethod("getFileExtension", String.class);
		method.setAccessible(true);
		
		assertEquals("Should be png", "png", (String) method.invoke(fileService, "test.png"));
		assertEquals("Should be doc", "doc", (String) method.invoke(fileService, "s.doc"));
		assertEquals("Should be jpeg", "jpeg", (String) method.invoke(fileService, "file.jpeg"));
		assertNull("Should be null", (String) method.invoke(fileService, "test"));
	}
	
	@Test
	public void testIsImageExtension() throws Exception
	{
		Method method = fileService.getClass().getDeclaredMethod("isImageExtension", String.class);
		method.setAccessible(true);
		
		assertEquals("png should be valid", true, (Boolean) method.invoke(fileService, "test.png"));
		assertEquals("PNG should be valid", true, (Boolean) method.invoke(fileService, "test.PNG"));
		assertEquals("jpg should be valid", true, (Boolean) method.invoke(fileService, "test.jpg"));
		assertEquals("JPG should be valid", true, (Boolean) method.invoke(fileService, "test.JPG"));
		assertEquals("jpeg should be valid", true, (Boolean) method.invoke(fileService, "test.jpeg"));
		assertEquals("JPEG should be valid", true, (Boolean) method.invoke(fileService, "test.JPEG"));
		assertEquals("gif should be valid", true, (Boolean) method.invoke(fileService, "test.gif"));
		assertEquals("GIF should be valid", true, (Boolean) method.invoke(fileService, "test.GIF"));
		assertEquals("doc should not be valid", false, (Boolean) method.invoke(fileService, "test.doc"));
		assertEquals("jpg3 should not be valid", false,(Boolean) method.invoke(fileService, "test.jpg3"));
		assertEquals("gi should not be valid", false,(Boolean) method.invoke(fileService, "test.gi"));
	}
	
	@Test
	public void testMakeSubDirectory() throws Exception
	{
		Method method = fileService.getClass().getDeclaredMethod("makeSubDirectory", String.class, String.class);
		method.setAccessible(true);
		
		for(int i=0; i < 10000; i++)
		{
			File created = (File) method.invoke(fileService, photoUploadDirectory, "photo");
			assertEquals("Directory should exist " + created.getAbsolutePath(), true, created.exists());
		}
	}
}
