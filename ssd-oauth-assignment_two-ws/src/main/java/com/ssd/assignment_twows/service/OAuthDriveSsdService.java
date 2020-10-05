package com.ssd.assignment_twows.service;

import org.springframework.web.multipart.MultipartFile;

public interface OAuthDriveSsdService {

	public void uploadFile(MultipartFile multipartFile) throws Exception;
}
