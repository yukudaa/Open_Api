package start.openApi.openapi.xml;

import lombok.Data;
import start.openApi.openapi.xml.Body;
import start.openApi.openapi.xml.Header;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "response")
public class Response {

    public Header header;
    public Body body;
}
