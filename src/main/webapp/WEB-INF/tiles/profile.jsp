<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url var="profilePhoto" value="/profilephoto/${userId}" />
<c:url var="editProfileAbout" value="/editprofileabout" />

<div class="row">
	<div class="col-md-10 col-md-offset-1">
		<div id="profile-photo-text">Photo Uploaded</div>
		<div class="profile-about">
			<div class="profile-image">
				<div>
					<img src="${profilePhoto}" id="profile-photo-img">
				</div>
				<div class="text-center">
					<a href="#" id="uploadLink">Upload Photo</a>
				</div>
			</div>
			
			<div class="profile-text">
				<c:choose>
					<c:when test="${profile.about == null}">
						Click 'edit' to add information about yourself to your profile.
					</c:when>
					<c:otherwise>
						<c:out value="${profile.about}"></c:out>
					</c:otherwise>
				</c:choose>
			</div>
			
			<div class="profile-about-edit">
				<a href="${editProfileAbout}">Edit</a>
			</div>
		</div>
		<c:url value="/upload-profile-photo" var="uploadPhotoLink"></c:url>
			<form method="post" enctype="multipart/form-data" action="${uploadPhotoLink}" id="photoUploadForm">
			<input type="file" accept="image/*" name="file" id="photoFileInput"></input>
			<input type="submit" value="upload" />
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
	</div>
</div>

<script>

function setUploadStatusText(text) {
	$("#profile-photo-text").text(text);
	
	window.setTimeout(function () {
		$("#profile-photo-text").text("");
	}, 2000);
}

function uploadSuccess(data) {
	
	$("#profile-photo-img").attr("src", "${profilePhoto}?t=" + new Date());
	
	$("#photoFileInput").val("");
	
	setUploadStatusText(data.message);
}

function uploadPhoto(event) {
	
	$.ajax({
		url: $(this).attr("action"),
		type: 'POST',
		data: new FormData(this),
		processData: false,
		contentType: false,
		success: uploadSuccess,
		error: function() { 
			setUploadStatusText("Server unreachable");
		}
	});
	
	event.preventDefault();
}

$(document).ready(function() {
	
	setUploadStatusText("Hello There");
	
	$("#uploadLink").click(function(event) {
		event.preventDefault();
		$("#photoFileInput").trigger('click');
	});
	
	$('#photoFileInput').change(function() {
		$('#photoUploadForm').submit();
	});
	
	$('#photoUploadForm').on('submit', uploadPhoto);
});
	
</script>