# Spring Boot Starter CROWD

Library for integrating CROWD authentication with Spring Boot / Security.

## Usage

Include the dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>nl.42</groupId>
    <artifactId>spring-boot-starter-crowd</artifactId>
    <version>${starter-crowd.version}</version>
</dependency>
```
 
And provide the required configuration:

```yaml
crowd:
  # enabled: true
  server: https://your-crowd
  application: app-dev
  password: password
```

During startup this will register a remote CROWD `AuthenticationProvider`.
The provider still has to be included in the `AuthenticationManager`.

## Configuration

### Properties

Properties that would previously have gone in `crowd.properties` can now be defined in the regular application YAML:

```yaml
crowd:
  properties:
    timeout: 200
```

### Groups

CROWD group names can be mapped to authentications using `crowd.roles`:

```yaml
crowd:
  roles:
    crowd-app-dev-admin: admin
    crowd-app-dev-user: user
```
