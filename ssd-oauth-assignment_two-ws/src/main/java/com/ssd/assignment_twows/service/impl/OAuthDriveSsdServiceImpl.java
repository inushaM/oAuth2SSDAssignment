package com.ssd.assignment_twows.service.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.ssd.assignment_twows.constant.OAuthApplicationSsdConstant;
import com.ssd.assignment_twows.service.OAuthAuthorizationSsdService;
import com.ssd.assignment_twows.service.OAuthDriveSsdService;
import com.ssd.assignment_twows.util.OAuthApplicationSsdConfig;

@Service
public class OAuthDriveSsdServiceImpl implements OAuthDriveSsdService {

	private Logger logger = LoggerFactory.getLogger(OAuthDriveSsdServiceImpl.class);

	private Drive driveService;

	@Autowired
	OAuthAuthorizationSsdService oAuthAuthorizationSsdService;

	@Autowired
	OAuthApplicationSsdConfig oAuthApplicationSsdConfig;

	@PostConstruct
	public void init() throws Exception {
		Credential credential = oAuthAuthorizationSsdService.getOAuthCredentials();
		driveService = new Drive.Builder(OAuthApplicationSsdConstant.HTTP_TRANSPORT, OAuthApplicationSsdConstant.JSON_FACTORY, credential)
				.setApplicationName(OAuthApplicationSsdConstant.APPLICATION_OAUTH_NAME).build();
	}

	//file upload to the drive
	@Override
	public void uploadFile(MultipartFile multipartFile) throws Exception {
		logger.debug("Inside Upload Service...");

		String path = oAuthApplicationSsdConfig.getTemporaryOAuthFolder();
		String fileName = multipartFile.getOriginalFilename();
		String contentType = multipartFile.getContentType();

		java.io.File transferedFile = new java.io.File(path, fileName);
		multipartFile.transferTo(transferedFile);

		//set the file name
		File fileMetadata = new File();
		fileMetadata.setName(fileName);
		
		FileContent mediaContent = new FileContent(contentType, transferedFile);
		File file = driveService.files().create(fileMetadata, mediaContent).setFields("id").execute();

		logger.debug("File ID: " + file.getName() + ", " + file.getId());
	}

}
