package nl._42.boot.crowd.rest;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "authentication-context")
@XmlAccessorType(XmlAccessType.FIELD)
class AuthenticationContext {

    private String username;
    private String password;

}
