```bash
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:22.0.4 start-dev
```

```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=spring -e MYSQL_DATABASE=spring -e MYSQL_USER=spring -e MYSQL_PASSWORD=spring mysql/mysql-server:latest
```