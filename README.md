
http://localhost:8080/swagger-ui/index.html
https://doc.netuno.org/docs/en/academy/server/database/hikaricp-database-connection-pool/

docker run --name resilience-jpa -p 5455:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=resilience-jpa -d postgres

docker run --name my-pgadmin -p 82:80 -e 'PGADMIN_DEFAULT_EMAIL=l@postgres.com' -e 'PGADMIN_DEFAULT_PASSWORD=postgres' -d dpage/pgadmin4

connection
host: host.docker.internal
database: postgres
user: postgres
password: postgres

##Links
https://skaveesh.medium.com/how-i-decoupled-resilience4j-circuit-breaker-from-the-code-with-aop-in-spring-boot-for-better-code-6af89038057d