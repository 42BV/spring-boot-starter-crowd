# Spring Boot Starter CROWD

Library for integrating CROWD authentication with Spring Boot / Security.

## Usage

Include the dependency in your Spring Boot application and configure:

```yaml
crowd:
  # enabled: true
  properties:
    crowd.server.url: https://your-crowd
    application:
      name: app-dev
      password: password
  roles:
    crowd-app-dev-admin: admin
    crowd-app-dev-user: user
```
