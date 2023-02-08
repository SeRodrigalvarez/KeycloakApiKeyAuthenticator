FROM quay.io/keycloak/keycloak:20.0.3

COPY artifact/* /opt/keycloak/providers/

EXPOSE 8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]