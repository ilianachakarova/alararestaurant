package alararestaurant.repository;

import alararestaurant.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /*
    select e.name, o.customer, oi.item_id
from orders as o join employees e on o.employee_id = e.id

join order_items oi on o.id = oi.id
group by e.name
order by e.name, o.id;
     */

    @Query("select o from Order as o join o.orderItems as oi join o.employee as e group by e " +
            "order by e.name, o.id" )
    List<Order>findAllFinishedOrdersByEmployeeName();
}
