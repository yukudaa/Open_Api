package start.openApi.openapi.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "header")
public class Header {

    public String resultCode;
    public String resultMsg;
}
