package nl._42.boot.crowd.rest;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

class Marshaller {

    private final Jaxb2Marshaller marshaller;

    Marshaller() {
        marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(AuthenticationContext.class.getPackage().getName());
    }

    String marshall(Object value) {
        StringWriter writer = new StringWriter();
        marshaller.marshal(value, new StreamResult(writer));
        return writer.toString();
    }

    Object unmarshal(String content) {
        return marshaller.unmarshal(new StreamSource(new StringReader(content)));
    }

}
