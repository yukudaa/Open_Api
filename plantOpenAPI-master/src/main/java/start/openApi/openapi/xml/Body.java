package start.openApi.openapi.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "body")
public class Body {

    @XmlElementWrapper(name="items")
    @XmlElement(name="item")
    public List<Item> items;
}
