```bash
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:22.0.4 start-dev
```

Options to retrieve principal (we set these are method parameters):
- authentication.getPrincipal()
- SecurityContextHolder.getContext().getAuthentication()
- @AuthenticationPrincipal CustomUser principal
- @AuthenticationPrincipal JwtAuthenticationToken token
- @AuthenticationPrincipal OidcUser user
