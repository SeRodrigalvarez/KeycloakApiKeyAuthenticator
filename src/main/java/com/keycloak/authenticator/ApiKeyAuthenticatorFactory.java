package com.keycloak.authenticator;

import java.util.List;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

public class ApiKeyAuthenticatorFactory implements AuthenticatorFactory {
	
	public static final String PROVIDER_ID = "api-key-authenticator";
	private static final Authenticator SINGLETON = new ApiKeyAuthenticator();
	
	private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
			AuthenticationExecutionModel.Requirement.REQUIRED,
			AuthenticationExecutionModel.Requirement.ALTERNATIVE,
			AuthenticationExecutionModel.Requirement.DISABLED
	};
	
	public Authenticator create(KeycloakSession session) {
		return SINGLETON;
	}

	public void init(Config.Scope config) {
		
	}

	public void postInit(KeycloakSessionFactory factory) {
		
	}

	public void close() {
		
	}

	public String getId() {
		return PROVIDER_ID;
	}

	public String getDisplayType() {
		return "API key authenticator";
	}

	public String getReferenceCategory() {
		return "Custom Authenticators";
	}

	public boolean isConfigurable() {
		return false;
	}

	public Requirement[] getRequirementChoices() {
		return REQUIREMENT_CHOICES;
	}

	public boolean isUserSetupAllowed() {
		return true;
	}

	public String getHelpText() {
		return "API key authenticator";
	}

	public List<ProviderConfigProperty> getConfigProperties() {
		return null;
	}

}
