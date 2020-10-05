package com.ssd.assignment_twows.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.ssd.assignment_twows.model.OAuthUploadFileSsd;
import com.ssd.assignment_twows.service.OAuthAuthorizationSsdService;
import com.ssd.assignment_twows.service.OAuthDriveSsdService;

@Controller
public class OAuthMainSsdController {

	private Logger loggerSsd = LoggerFactory.getLogger(OAuthMainSsdController.class);

	@Autowired
	OAuthAuthorizationSsdService oAuthAuthorizationSsdService;

	@Autowired
	OAuthDriveSsdService oAuthDriveSsdService;

	@GetMapping("/")
	public String showOAuthHomePage() throws Exception {
		if (oAuthAuthorizationSsdService.isOAuthUserAuthenticated()) {
			loggerSsd.debug("User is authenticated. Redirecting to home...");
			return "redirect:/home";
		} else {
			loggerSsd.debug("User is not authenticated. Redirecting to sso...");
			return "redirect:/login";
		}
	}

	@GetMapping("/login")
	public String goOAuthToLogin() {
		return "index.html";
	}

	@GetMapping("/home")
	public String goOAuthToHome() {
		return "home.html";
	}

	@GetMapping("/googlesignin")
	public void doOAuthGoogleSignIn(HttpServletResponse response) throws Exception {
		loggerSsd.debug("SSO Called...");
		response.sendRedirect(oAuthAuthorizationSsdService.authenticateOAuthUserViaGoogle());
	}

	@GetMapping("/oauth/callback")
	public String saveOAuthAuthorizationCode(HttpServletRequest request) throws Exception {
		loggerSsd.debug("SSO Callback invoked...");
		String code = request.getParameter("code");
		loggerSsd.debug("SSO Callback Code Value..., " + code);

		if (code != null) {
			oAuthAuthorizationSsdService.exchangeOAuthCodeForTokens(code);
			return "redirect:/home";
		}
		return "redirect:/login";
	}

	@GetMapping("/logout")
	public String logoutOAuth(HttpServletRequest request) throws Exception {
		loggerSsd.debug("Logout invoked...");
		oAuthAuthorizationSsdService.removeOAuthUserSession(request);
		return "redirect:/login";
	}

	@PostMapping("/upload")
	public String uploadOAuthFile(HttpServletRequest request, @ModelAttribute OAuthUploadFileSsd uploadedFile) throws Exception {
		MultipartFile multipartFile = uploadedFile.getMultipartFile();
		oAuthDriveSsdService.uploadFile(multipartFile);
		return "redirect:/home?status=success";
	}
}
