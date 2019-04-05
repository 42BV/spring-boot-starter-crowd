# Spring Boot Starter CROWD

Library for integrating CROWD authentication with Spring Boot / Security.

## Usage

Include the dependency in your Spring Boot application and configure:

```yaml
crowd:
  # enabled: true
  property:
    crowd.server.url: https://your-crowd
    application:
      name: app-dev
      password: password
    session:
      validationinterval: 0
  roles:
    crowd-app-dev-admin: admin
    crowd-app-dev-user: user
```

And provide a crowd-ehcache.xml on the classpath:

```yaml
<ehcache>
	<diskStore path="java.io.tmpdir" />
	<defaultCache maxElementsInMemory="50000" eternal="false"
		overflowToDisk="false" timeToIdleSeconds="3600" timeToLiveSeconds="3600"
		diskPersistent="false" diskExpiryThreadIntervalSeconds="120" />
</ehcache>
```

Spring will automatically register a CROWD authentication provider that can
be used for authentication.
