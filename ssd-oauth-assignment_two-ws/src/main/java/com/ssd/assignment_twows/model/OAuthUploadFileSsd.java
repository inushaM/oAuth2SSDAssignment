package com.ssd.assignment_twows.model;

import org.springframework.web.multipart.MultipartFile;

public class OAuthUploadFileSsd {

	private MultipartFile multipartFile;

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}
}
