# Keycloak Api Key Authenticator Module

Keycloak doesn't support api keys out of the box, so a custom authenticator module must be created. This authenticator checks if there is an user with that api key. The api key must be set in the user attributes with the key "apiKey". The default direct grant flow must be cloned and the autheticator must be added.

If a request to the token endpoint with the param "apiKey" is sent, the authenticator will check the api key.