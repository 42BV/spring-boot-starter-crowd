package nl._42.boot.crowd.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl._42.boot.crowd.CrowdProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class CrowdClient {

    private final CrowdProperties crowdProperties;

    private final Jaxb2Marshaller marshaller;
    private final RestTemplate template;

    public CrowdClient(CrowdProperties crowdProperties) {
        this.crowdProperties = crowdProperties;

        marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(AuthenticationContext.class.getPackage().getName());

        template = new RestTemplate();
    }

    public String authenticate(String username, String password) throws AuthenticationException {
        log.info("Validating CROWD password");

        AuthenticationContext context = new AuthenticationContext();
        context.setUsername(username);
        context.setPassword(password);

        try {
            String body = marshall(context);
            Session session = (Session) send("/session?validate-password=true", HttpMethod.POST, body);
            log.info("CROWD password validated");

            return session.getToken();
        } catch (RestClientException rce) {
            log.error("Could not authenticate via CROWD", rce);
            throw new AuthenticationServiceException("Could not authenticate", rce);
        }
    }

    private String marshall(Object value) {
        StringWriter writer = new StringWriter();
        marshaller.marshal(value, new StreamResult(writer));
        return writer.toString();
    }

    public User getUser(String username) {
        log.info("Retrieving user");

        try {
            String path = String.format("/user?username=%s", username);
            return (User) send(path, HttpMethod.GET, null);
        } catch (RestClientException rce) {
            log.error("Could not retrieve CROWD user", rce);
            throw new AuthenticationServiceException("Could not retrieve CROWD user", rce);
        }
    }

    public Set<String> getGroups(String username) {
        log.info("Retrieving groups");

        try {
            String path = String.format("/user/group/nested?username=%s", username);
            Groups groups = (Groups) send(path, HttpMethod.GET, null);
            return groups.getGroups().stream().map(Group::getName).collect(Collectors.toSet());
        } catch (RestClientException rce) {
            log.error("Could not retrieve CROWD groups", rce);
            return Collections.emptySet();
        }
    }

    private Object send(String path, HttpMethod method, String body) {
        String url = crowdProperties.getUrl(path);
        HttpEntity<String> entity = build(body);
        ResponseEntity<String> response = template.exchange(url, method, entity, String.class);
        return marshaller.unmarshal(new StreamSource(new StringReader(response.getBody())));
    }

    private HttpEntity<String> build(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        crowdProperties.setAuthentication(headers);

        return new HttpEntity<>(body, headers);
    }

}
