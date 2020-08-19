package com.springbootdemo.service;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springbootdemo.exceptions.ImageTooSmallException;
import com.springbootdemo.exceptions.InvalidFileException;
import com.springbootdemo.model.dto.FileInfo;

@Service
public class FileService {
	
	@Value("${photo.file.extensions}")
	private String fileExtensions;
	
	private Random random = new Random();
	
	private String getFileExtension(String fileName)
	{
		int dot = fileName.lastIndexOf(".");
		
		if(dot < 0) return null;
		
		return fileName.substring(dot+1).toLowerCase();
	}
	
	private boolean isImageExtension(String ext)
	{
		String testExt = ext.toLowerCase();
		testExt = testExt.substring(testExt.lastIndexOf(".")+1);
		
		for(String validExt: fileExtensions.split(","))
		{	
			if(testExt.equals(validExt)) return true;
		}
		
		return false;
	}
	
	private File makeSubDirectory(String basePath, String prefix)
	{
		int nDirectory = random.nextInt(1000);
		String sDirectory = String.format("%s%03d", prefix, nDirectory);
		
		File directory = new File(basePath, sDirectory);
		
		if(!directory.exists())
		{
			directory.mkdir();
		}
		
		return directory;
	}

	public FileInfo saveImageFile(MultipartFile file, String baseDirectory, String subDirPrefix, String filePrefix, int width, int height) throws InvalidFileException, IOException, ImageTooSmallException
	{
		int nFileName = random.nextInt(1000);
		String fileName = String.format("%s%03d", filePrefix, nFileName);
		
		String extension = getFileExtension(file.getOriginalFilename());
		if(extension == null)
			throw new InvalidFileException("No file extension.");
		
		if(!isImageExtension(extension))
			throw new InvalidFileException("Invalid file extension");
		
		File subDirectory = makeSubDirectory(baseDirectory, subDirPrefix);
		Path filePath = Paths.get(subDirectory.getCanonicalPath(), fileName + "." + extension);
		
		Files.deleteIfExists(filePath);
		
		BufferedImage resizedImage = resizeImage(file, width, height);
	
		Files.copy(file.getInputStream(), filePath);
		ImageIO.write(resizedImage, extension, filePath.toFile());
		
		return new FileInfo(fileName, extension, subDirectory.getName(), baseDirectory);
	}

	private BufferedImage resizeImage(MultipartFile inputFile, int width, int height) throws IOException, ImageTooSmallException 
	{
		BufferedImage image = ImageIO.read(inputFile.getInputStream());
		
		if(image.getWidth() < width || image.getHeight() < height)
			throw new ImageTooSmallException();
		
		double widthScale = (double) width/image.getWidth();
		double heightScale = (double) height/image.getHeight();
		
		double scale = Math.max(widthScale, heightScale);
		
		int newWidth = (int) (scale * image.getWidth());
		int newHeight = (int) (scale * image.getHeight());
		
		BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, image.getType());
		
		Graphics2D g = scaledImage.createGraphics();

		AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
		
		g.drawImage(image, transform, null);
		
		return scaledImage.getSubimage(0, 0, width, height);
	}
}
