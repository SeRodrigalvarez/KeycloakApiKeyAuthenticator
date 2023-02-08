package com.keycloak.authenticator;

import java.util.stream.Collectors;
import java.util.stream.Stream;
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

		/**
		 * Prior Keycloak 19.0.0, use: 
		 * 
		 * List<UserModel> result = context.getSession().userStorageManager().searchForUserByUserAttribute(API_KEY_PARAM, apiKey, context.getRealm());
		 * 
		 * Keycloak migration 19.0.0 guide: https://www.keycloak.org/docs/latest/upgrading/index.html
		 */
		
		Stream<UserModel> result = context.getSession().users().searchForUserByUserAttributeStream(context.getRealm(), API_KEY_PARAM, apiKey);

		List<UserModel> userList = result.collect(Collectors.toList());

		
		if (userList.size() != 1) {
			context.failure(AuthenticationFlowError.UNKNOWN_USER);
			return;
		}

		UserModel keycloakUser = userList.get(0);
		
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
