package com.springbootdemo.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.owasp.html.PolicyFactory;

@Entity
@Table(name="profile")
public class Profile 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@OneToOne(targetEntity=SiteUser.class)
	@JoinColumn(name="user_id", nullable=false)
	private SiteUser user;
	
	@Column(name="about", length=5000)
	@Size(max=5000, message="{editprofile.about.size}")
	private String about;
	
	@Column(name="photo_directory", length=10)
	private String photoDirectory;
	
	@Column(name="photo_name", length=10)
	private String photoName;
	
	@Column(name="photo_ext", length=5)
	private String photoExt;

	@ManyToMany
	@JoinTable(name="profile_interests", 
			   joinColumns= { @JoinColumn(name="profile_id") },
			   inverseJoinColumns= { @JoinColumn(name="interest_id")})
	private Set<Interest> interests;
	
	public Profile() {
		
	}
	
	/* Creating constructor to make testing users easier */
	public Profile(SiteUser user) {
		this.user = user;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SiteUser getUser() {
		return user;
	}

	public void setUser(SiteUser user) {
		this.user = user;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String text) {
		this.about = text;
	}
	
	public String getPhotoDirectory() {
		return photoDirectory;
	}

	public void setPhotoDirectory(String photoDirectory) {
		this.photoDirectory = photoDirectory;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getPhotoExt() {
		return photoExt;
	}

	public void setPhotoExt(String photoExt) {
		this.photoExt = photoExt;
	}

	public Set<Interest> getInterests() {
		return interests;
	}

	public void setInterests(Set<Interest> interests) {
		this.interests = interests;
	}

	/* Create a profile that is suitable for displaying a JSP */
	public void safeCopyFrom(Profile other) {
		if(other.about != null){
			this.about = other.about;
		}
		
		if(other.interests != null) {
			this.interests = other.interests;
		}
	}

	/* Create a profile that is suitable for saving */
	public void safeMergeFrom(Profile webProfile, PolicyFactory htmlPolicy) {
		if(webProfile.about != null) {
			this.about = htmlPolicy.sanitize(webProfile.about);
		}
	}
	
	public void setPhotoDetails(FileInfo file){
		photoDirectory = file.getSubDirectory();
		photoName = file.getBaseName();
		photoExt = file.getExt();
	}
	
	public Path getPhoto(String baseDirectory){
		if(this.photoName == null) {
			return null;
		}
		
		return Paths.get(baseDirectory, this.photoDirectory + "\\" + this.photoName + "." + this.photoExt);
	}

	public void addInterest(Interest interest) {
		this.interests.add(interest);
	}

	public void removeInterest(String interestName) {
		this.interests.remove(new Interest(interestName));
	}

	@Override
	public String toString() {
		return "Profile [id=" + id + ", user=" + user + ", about=" + about + ", photoDirectory=" + photoDirectory
				+ ", photoName=" + photoName + ", photoExt=" + photoExt + ", interests=" + interests + "]";
	}
}
