package com.ssd.assignment_twows.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties
public class OAuthApplicationSsdConfig {

	@Value("${google.oauth.callback.uri}")
	private String CALLBACK_URI_OAUTH;

	@Value("${google.secret.key.path}")
	private Resource driveOAuthSecretKeys;

	@Value("${google.credentials.folder.path}")
	private Resource credentialsOAuthFolder;

	@Value("${myapp.temp.path}")
	private String temporaryOAuthFolder;

	public String getCALLBACK_URI_OAUTH() {
		return CALLBACK_URI_OAUTH;
	}

	public void setCALLBACK_URI_OAUTH(String cALLBACK_URI_OAUTH) {
		CALLBACK_URI_OAUTH = cALLBACK_URI_OAUTH;
	}

	public Resource getDriveOAuthSecretKeys() {
		return driveOAuthSecretKeys;
	}

	public void setDriveOAuthSecretKeys(Resource driveOAuthSecretKeys) {
		this.driveOAuthSecretKeys = driveOAuthSecretKeys;
	}

	public Resource getCredentialsOAuthFolder() {
		return credentialsOAuthFolder;
	}

	public void setCredentialsOAuthFolder(Resource credentialsOAuthFolder) {
		this.credentialsOAuthFolder = credentialsOAuthFolder;
	}

	public String getTemporaryOAuthFolder() {
		return temporaryOAuthFolder;
	}

	public void setTemporaryOAuthFolder(String temporaryOAuthFolder) {
		this.temporaryOAuthFolder = temporaryOAuthFolder;
	}
}
