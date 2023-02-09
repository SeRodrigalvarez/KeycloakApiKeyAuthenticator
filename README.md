# Keycloak Api Key Authenticator Module

## Intro

Keycloak doesn't support API keys out of the box, so a custom authenticator module must be created. This custom authenticator checks if there is an user with that API key. The API key must be set in the user attributes with the key "apiKey". The default direct grant flow must be cloned and the autheticator must be added. If an user is found with that API key, a bearer token is returned in the respose.

## Motivation

You could manage the API keys directly in your API, but this approach has several problems:
- You have to store the API keys elsewhere, even though it is related to the authentication of the user and it should be managed by Keycloak.
- If you use Keycloak roles and permissions, you don't have a way to restrict your API based on that using API keys, as this information is in Keycloak.

So, the best approach is that Keycloak manages the API keys.

## Requirements

To run this example, you need:

- JDK 11
- Maven
- Docker
- curl, or other http client

## How to run?

Dockerfile uses Keycloak 20.

First, you have to generate the custom authenticator's jar deployment file:

```
mvn package
```

Build the Docker image:

```
docker build --tag api-key-keycloak-example .
```

It will build a Keycloak image with an example realm. This example realm has a modified direct grant flow that accepts an apiKey param in the token endpoint. It also has an example user with username `user-name`, password `password` and API key `some-random-api-key`. The API key can be found in the user attributes of that user.

Run the Docker image:

```
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin api-key-keycloak-example
```

Keycloak is up and running now and exposed on port 8080 with an admin user with username `admin` and password `admin`. You can open Keycloak admin console at: `http://localhost:8080/`. You can access the modified direct grant flow under Authentication in the Example-Realm realm.

You can get a bearer token with the usual username and password credentials:

```
curl --location --request POST 'http://localhost:8080/realms/Example-Realm/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'username=user-example' \
--data-urlencode 'password=password' \
--data-urlencode 'client_id=example-client' \
--data-urlencode 'grant_type=password'
```

But you can also get a bearer token with an API key:

```
curl --location --request POST 'http://localhost:8080/realms/Example-Realm/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=example-client' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'apiKey=some-random-api-key'
```

You are basically exchanging an API key for a bearer token now.

## Recomendations

I recommend you to make the previous request transparently in the Back-End when you receive a request to your API with an API key. You have to do it before the code block that validates the bearer token, as you want to exchange the API key for a bearer token. The bearer token will eventually expire and you must manage this bearer token for the API key user, as the user only knows about its API key. I recommend you to use a cache with eviction time whose key is the user's API key and whose value is the bearer token. The registry should be evicted when the bearer token expries (or before, depending on how frequently the eviction process runs). When you receive a request to your API with an API key, check if there is a registry with that API key in the cache:
1. If there is one, append that bearer token to the request.
2. If there is none, make the request to the token endpoint with the API key. When you receive the token, extract the expiration time from it and insert the bearer token in the cache with the corresponding eviction time. Then, you can finally append the bearer token to the request.