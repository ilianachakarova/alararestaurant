package alararestaurant.domain.dtos.orders_dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderSeedRootDto {
    @XmlElement(name = "order")
    private List<OrderSeedDto> orders;

    public OrderSeedRootDto() {
    }

    public List<OrderSeedDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderSeedDto> orders) {
        this.orders = orders;
    }
}
