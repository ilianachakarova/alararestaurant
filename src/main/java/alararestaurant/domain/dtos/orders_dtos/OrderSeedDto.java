package alararestaurant.domain.dtos.orders_dtos;

import alararestaurant.adaptors.LocalDateTimeAdaptor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderSeedDto {
    @XmlElement(name = "customer")
    private String customer;
    @XmlElement(name = "employee")
    private String employee;
    @XmlElement(name = "date-time")
    @XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
    private LocalDateTime dateTime;
    @XmlElement(name = "type")
    private String type;
    @XmlElement(name = "items")
    private OrderListRootDto orderListRootDto;

    public OrderSeedDto() {
    }
    @NotNull
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    @NotNull
    @Length(min = 3, max = 30)
    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }
    @NotNull

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    @NotNull
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OrderListRootDto getOrderListRootDto() {
        return orderListRootDto;
    }

    public void setOrderListRootDto(OrderListRootDto orderListRootDto) {
        this.orderListRootDto = orderListRootDto;
    }
}
