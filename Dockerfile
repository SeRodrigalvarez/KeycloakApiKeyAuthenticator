FROM quay.io/keycloak/keycloak:20.0.3

COPY artifact/* /opt/keycloak/providers/

ADD example-realm-export.json /tmp/

RUN /opt/keycloak/bin/kc.sh import --file /tmp/example-realm-export.json

EXPOSE 8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]