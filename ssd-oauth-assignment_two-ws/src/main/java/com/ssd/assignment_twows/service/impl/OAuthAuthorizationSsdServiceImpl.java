package com.ssd.assignment_twows.service.impl;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.ssd.assignment_twows.constant.OAuthApplicationSsdConstant;
import com.ssd.assignment_twows.service.OAuthAuthorizationSsdService;
import com.ssd.assignment_twows.util.OAuthApplicationSsdConfig;

@Service
public class OAuthAuthorizationSsdServiceImpl implements OAuthAuthorizationSsdService {

	private Logger logger = LoggerFactory.getLogger(OAuthAuthorizationSsdServiceImpl.class);
	private GoogleAuthorizationCodeFlow flow;
	private FileDataStoreFactory dataStoreFactory;

	@Autowired
	private OAuthApplicationSsdConfig config;

	@PostConstruct
	public void init() throws Exception {
		InputStreamReader reader = new InputStreamReader(config.getDriveOAuthSecretKeys().getInputStream());
		dataStoreFactory = new FileDataStoreFactory(config.getCredentialsOAuthFolder().getFile());

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(OAuthApplicationSsdConstant.JSON_FACTORY, reader);
		flow = new GoogleAuthorizationCodeFlow.Builder(OAuthApplicationSsdConstant.HTTP_TRANSPORT, OAuthApplicationSsdConstant.JSON_FACTORY, clientSecrets,
				OAuthApplicationSsdConstant.SCOPES).setDataStoreFactory(dataStoreFactory).build();
	}

	@Override
	public boolean isOAuthUserAuthenticated() throws Exception {
		Credential credential = getOAuthCredentials();
		if (credential != null) {
			boolean isTokenOAuthValid = credential.refreshToken();
			logger.debug("isTokenValid, " + isTokenOAuthValid);
			return isTokenOAuthValid;
		}
		return false;
	}

	@Override
	public Credential getOAuthCredentials() throws IOException {
		return flow.loadCredential(OAuthApplicationSsdConstant.USER_OAUTH_IDENTIFIER_KEY);
	}

	@Override
	public String authenticateOAuthUserViaGoogle() throws Exception {
		GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
		String redirectOAuthUrl = url.setRedirectUri(config.getCALLBACK_URI_OAUTH()).setAccessType("offline").build();
		logger.debug("redirectUrl, " + redirectOAuthUrl);
		return redirectOAuthUrl;
	}

	@Override
	public void exchangeOAuthCodeForTokens(String code) throws Exception {
		GoogleTokenResponse googleTokenResponse = flow.newTokenRequest(code).setRedirectUri(config.getCALLBACK_URI_OAUTH()).execute();
		flow.createAndStoreCredential(googleTokenResponse, OAuthApplicationSsdConstant.USER_OAUTH_IDENTIFIER_KEY);
	}

	@Override
	public void removeOAuthUserSession(HttpServletRequest request) throws Exception {
		dataStoreFactory.getDataStore(config.getCredentialsOAuthFolder().getFilename()).clear();
	}

}
