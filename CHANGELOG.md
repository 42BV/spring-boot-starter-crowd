# Release notes

## 1.0.0
 - Upgraded to latest `crowd-integration-springsecurity:3.4.3`
 - Using the CROWD REST client instead of SOAP
 - Rename all `crowd.property` prefixed properties to `crowd.properties`, e.g. `crowd.properties.application.name=test`

## 0.0.2
 - `crowd.properties` is no longer needed on classpath
 - `crowd-ehcache.xml` is provided by default, no need to put on classpath
 - Using default role mapping functionality from Atlassian, no config changes needed

## 0.0.1
 - Initial release, authentication fully works
