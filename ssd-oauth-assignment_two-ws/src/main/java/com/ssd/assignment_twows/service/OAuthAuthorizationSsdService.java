package com.ssd.assignment_twows.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.Credential;

public interface OAuthAuthorizationSsdService {

	public boolean isOAuthUserAuthenticated() throws Exception;

	public Credential getOAuthCredentials() throws IOException;

	public String authenticateOAuthUserViaGoogle() throws Exception;

	public void exchangeOAuthCodeForTokens(String code) throws Exception;
	
	public void removeOAuthUserSession(HttpServletRequest request) throws Exception;
}
