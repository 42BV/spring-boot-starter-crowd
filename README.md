# Spring Boot Starter CROWD

Library for integrating CROWD authentication with Spring Boot / Security.

## Usage

Include the dependency in your Spring Boot application and configure:

```yaml
crowd:
  # enabled: true
  server: https://your-crowd
  application: app-dev
  password: password
```

This will automatically register a REST based remote CROWD Authentication Provider.

### Properties

Properties that would previously have gone in `crowd.properties` can now be defined in the regular application YAML:

```yaml
crowd:
  properties:
    crowd.server.url: https://your-crowd
    application:
      name: app-dev
      password: password
```

### Group to authority mapping

Granted CROWD group names can be mapped to authentications using a `crowd.roles` prefix:

```yaml
crowd:
  roles:
    crowd-app-dev-admin: admin
    crowd-app-dev-user: user
```
