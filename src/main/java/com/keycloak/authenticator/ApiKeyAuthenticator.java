package com.keycloak.authenticator;

import java.util.List;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class ApiKeyAuthenticator implements Authenticator {

	private static final String API_KEY_PARAM = "apiKey";

	public void authenticate(AuthenticationFlowContext context) {
		
		String apiKey = context.getHttpRequest().getFormParameters().getFirst(API_KEY_PARAM);
		
		if (apiKey==null) {
			context.failure(AuthenticationFlowError.UNKNOWN_USER);
			return;
		}
		
		List<UserModel> result = context.getSession().userStorageManager().searchForUserByUserAttribute(API_KEY_PARAM, apiKey, context.getRealm());
		
		if (result.isEmpty() || result.size() != 1) {
			context.failure(AuthenticationFlowError.UNKNOWN_USER);
			return;
		}
		
		UserModel keycloakUser = result.get(0);
		
		context.setUser(keycloakUser);
		context.success();
	}

	public void action(AuthenticationFlowContext context) {
	}

	public boolean requiresUser() {
		return false;
	}

	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		return true;
	}

	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
	}

	public void close() {
	}

}
