# Configurations

## Development Configuration

http://localhost:8080

- **Components Started:**
    - Keycloak
        - User: Max
        - Password: max01
    - PostgreSQL

- **Startup Mechanism:**
    - Utilizes the Spring container feature to start Docker containers if they are not already running.

- **Database Setup:**
    - PostgreSQL is initialized using Flyway for database migrations.

- **Keycloak Configuration:**
    - Keycloak is preconfigured with a test user for development and testing purposes.

## Production Configuration

### Required Environment Variables:

- **Database Configuration:**
    - `DB_URL`  
      The URL for connecting to the production database.
    - `DB_USERNAME`  
      The username for authenticating with the production database.
    - `DB_PASSWORD`  
      The password for authenticating with the production database.

- **Keycloak Configuration:**
    - `KEYCLOAK_ISSUER_URI`  
      The issuer URI for the Keycloak authentication server.
    - `KEYCLOAK_CLIENT_ID`  
      The client ID for the application in Keycloak.
    - `KEYCLOAK_CLIENT_SECRET`  
      The client secret for the application in Keycloak.
