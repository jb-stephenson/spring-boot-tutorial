<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url var="profilePhoto" value="/profilephoto/${userId}" />
<c:url var="editProfileAbout" value="/editprofileabout" />
<c:url var="saveInterest" value="/save-interest" />
<c:url var="deleteInterest" value="/delete-interest" />

<div class="row">
	<div class="col-md-10 col-md-offset-1">
		<div id="profile-photo-text">Photo Uploaded</div>
		
		<div id="interest-div">
			<ul id="interestList">
				<c:choose>
					<c:when test="${empty profile.interests}">
						<li>Add Your Interests Here</li>
					</c:when>
					<c:otherwise>
						<c:forEach var="interest" items="${profile.interests}">
							<li>${interest}</li>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		
		<div class="profile-about">
			<div class="profile-image">
				<div>
					<img src="${profilePhoto}" id="profile-photo-img">
				</div>
				<div class="text-center">
					<c:if test="${ownProfile == true}">
						<a href="#" id="uploadLink">Upload Photo</a>
					</c:if>
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
				<c:if test="${ownProfile == true}">
					<a href="${editProfileAbout}">Edit</a>
				</c:if>
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

function saveInterest(text) {
	editInterest(text, "${saveInterest}");
}

function deleteInterest(text) {
	editInterest(text, "${deleteInterest}");
}

function editInterest(text, actionUrl) {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
		jqXHR.setRequestHeader(header, token);
	});
	
	$.ajax({
		"url": actionUrl,
		data: {
			"name": text
		},
		type: "POST",
		
		success: function() {
			alert("ok");
		},
		error: function() {
			alert("error");
		}
	});
}

$(document).ready(function() {
	
	setUploadStatusText("");
	
	$("#interestList").tagit({

		afterTagRemoved: function(event, ui) {
			deleteInterest(ui.tagLabel);
		},
	
		afterTagAdded: function(event, ui) {
			if(ui.duringInitialization != true) {
				saveInterest(ui.tagLabel);
			}
		},
		
		caseSensitive: false,
		allowSpaces: true,
		tagLimit: 10,
		readOnly: '${ownProfile}' == 'false'
	});
	
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