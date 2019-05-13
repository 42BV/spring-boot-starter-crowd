package nl._42.boot.crowd;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CrowdPropertiesTest {

    @Test
    public void get_properties() {
        Properties values = new Properties();
        values.put(CrowdProperties.SERVER_PROPERTY, "http://mycrowd");
        values.put(CrowdProperties.APPLICATION_PROPERTY, "application");
        values.put(CrowdProperties.PASSWORD_PROPERTY, "password");

        CrowdProperties properties = new CrowdProperties();
        properties.setProperties(values);

        Properties result = properties.getProperties();
        assertEquals("http://mycrowd", result.get(CrowdProperties.SERVER_PROPERTY));
        assertEquals("application", result.get(CrowdProperties.APPLICATION_PROPERTY));
        assertEquals("password", result.get(CrowdProperties.PASSWORD_PROPERTY));
    }

    @Test
    public void get_properties_sugar() {
        CrowdProperties properties = new CrowdProperties();
        properties.setServer("http://mycrowd");
        properties.setApplication("application");
        properties.setPassword("password");

        Properties result = properties.getProperties();
        assertEquals("http://mycrowd", result.get(CrowdProperties.SERVER_PROPERTY));
        assertEquals("application", result.get(CrowdProperties.APPLICATION_PROPERTY));
        assertEquals("password", result.get(CrowdProperties.PASSWORD_PROPERTY));
    }

    @Test
    public void get_properties_missing_server() {
        CrowdProperties properties = new CrowdProperties();
        properties.setApplication("application");
        properties.setPassword("password");

        try {
            properties.getProperties();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            assertEquals(
                    "Missing required property 'crowd.properties.crowd.server.url' either specify "
                            + "this property or disable CROWD using 'crowd.enabled=false'",
                ise.getMessage()
            );
        }
    }

    @Test
    public void get_properties_missing_application() {
        CrowdProperties properties = new CrowdProperties();
        properties.setServer("http://mycrowd");
        properties.setPassword("password");

        try {
            properties.getProperties();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            assertEquals(
                    "Missing required property 'crowd.properties.application.name' either specify "
                            + "this property or disable CROWD using 'crowd.enabled=false'",
                    ise.getMessage()
            );
        }
    }

    @Test
    public void get_properties_missing_password() {
        CrowdProperties properties = new CrowdProperties();
        properties.setServer("http://mycrowd");
        properties.setApplication("application");

        try {
            properties.getProperties();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            assertEquals(
                    "Missing required property 'crowd.properties.application.password' either specify "
                            + "this property or disable CROWD using 'crowd.enabled=false'",
                    ise.getMessage()
            );
        }
    }

}
