FROM java:8-jre-alpine

ENV KEYCLOAK_VERSION 10.0.0

 RUN   apk update \                                                                                                                                                                                                                        
  &&   apk add ca-certificates wget \                                                                                                                                                                                                      
  &&   update-ca-certificates

RUN wget https://downloads.jboss.org/keycloak/${KEYCLOAK_VERSION}/keycloak-${KEYCLOAK_VERSION}.tar.gz

RUN tar xvf keycloak-${KEYCLOAK_VERSION}.tar.gz && rm keycloak-${KEYCLOAK_VERSION}.tar.gz

WORKDIR keycloak-${KEYCLOAK_VERSION}

COPY artifact/* standalone/deployments/

# ADD import-realm.json /tmp/ # Uncomment if you want to use an imported realm

#add admin user
RUN ./bin/add-user-keycloak.sh -r master -u admin -p admin

EXPOSE 8080

ENTRYPOINT ["./bin/standalone.sh", "-Dkeycloak.profile.feature.upload_scripts=enabled", \
# "-Dkeycloak.import=/tmp/import-realm.json", \ # Used to import realm
"-b", "0.0.0.0"]