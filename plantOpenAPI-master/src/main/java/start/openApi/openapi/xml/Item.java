package start.openApi.openapi.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "item")
public class Item {

    private String code;
    private String codeNm;
}
