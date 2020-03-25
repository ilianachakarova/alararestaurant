package alararestaurant.domain.dtos.orders_dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderListRootDto {
    @XmlElement(name = "item")
    private List<OrderListDto> items;

    public OrderListRootDto() {
    }

    public List<OrderListDto> getItems() {
        return items;
    }

    public void setItems(List<OrderListDto> items) {
        this.items = items;
    }
}
