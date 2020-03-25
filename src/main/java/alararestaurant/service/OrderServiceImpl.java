package alararestaurant.service;

import alararestaurant.domain.dtos.orders_dtos.OrderListDto;
import alararestaurant.domain.dtos.orders_dtos.OrderSeedDto;
import alararestaurant.domain.dtos.orders_dtos.OrderSeedRootDto;
import alararestaurant.domain.entities.*;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.repository.OrderItemRepository;
import alararestaurant.repository.OrderRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import alararestaurant.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final EmployeeRepository employeeRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final static String ORDERS_FILE = "src/main/resources/files/orders.xml";

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, FileUtil fileUtil, XmlParser xmlParser, ValidationUtil validationUtil, EmployeeRepository employeeRepository, OrderItemRepository orderItemRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.employeeRepository = employeeRepository;
        this.orderItemRepository = orderItemRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Boolean ordersAreImported() {

      return this.orderRepository.count() > 0;
    }

    @Override
    public String readOrdersXmlFile() throws IOException {

        return this.fileUtil.readFile(ORDERS_FILE);
    }

    @Override
    public String importOrders() throws JAXBException, FileNotFoundException {
        StringBuilder importResult = new StringBuilder();
        OrderSeedRootDto orderSeedRootDto = this.xmlParser.parseXml(OrderSeedRootDto.class, ORDERS_FILE);
        List<OrderSeedDto> orderSeedDtoList = orderSeedRootDto.getOrders();
        for (OrderSeedDto orderSeedDto : orderSeedDtoList) {
            if(this.validationUtil.isValid(orderSeedDto)){
                Employee employee = this.employeeRepository.findByName(orderSeedDto.getEmployee()).orElse(null);
                if(employee!=null){
                   List<OrderItem>items = this.createListOfItems(orderSeedDto.getOrderListRootDto().getItems());
                   if(items!=null){
                       Order order = this.modelMapper.map(orderSeedDto, Order.class);
                       order.setDateTime(orderSeedDto.getDateTime());
                       order.setOrderItems(items);
                       order.setType(OrderType.valueOf(orderSeedDto.getType().toUpperCase()));
                       order.setEmployee(employee);

                       this.orderRepository.saveAndFlush(order);
                       items.forEach(orderItem -> orderItem.setOrder(order));

                       importResult.append("Successfully imported Order ").append(System.lineSeparator());
                   }
                }
            }else {
                importResult.append("Invalid data").append(System.lineSeparator());
            }
        }

        return importResult.toString().trim();
    }

    private List<OrderItem> createListOfItems(List<OrderListDto> items) {
        List<OrderItem>existingItems = new ArrayList<>();

        for (OrderListDto orderListDto: items) {
            if(this.validationUtil.isValid(orderListDto)){
                Item item = this.itemRepository.findByName(orderListDto.getName()).orElse(null);
                if(item == null){
                    return null;
                }else {
                    existingItems.add(new OrderItem(item,orderListDto.getQuantity()));
                }
            }
        }
        return existingItems;
    }

    @Override
    public String exportOrdersFinishedByTheBurgerFlippers() {
        StringBuilder exportResult = new StringBuilder();
        List<Order>orders = this.orderRepository.findAllFinishedOrdersByEmployeeName();

        for (Order order : orders) {
            exportResult.append("Name: ").append(order.getEmployee().getName()).append(System.lineSeparator());
            exportResult.append("Orders").append(System.lineSeparator());
                exportResult.append("Customer: ").append(order.getCustomer()).append(System.lineSeparator());
                exportResult.append("Items: ").append(System.lineSeparator());
                order.getOrderItems().forEach(orderItem -> {
                    exportResult.append("   Name: ").append(orderItem.getItem().getName()).append(System.lineSeparator());
                    exportResult.append("   Price: ").append(orderItem.getItem().getPrice()).append(System.lineSeparator());
                    exportResult.append("   Quantity: ").append(orderItem.getQuantity()).append(System.lineSeparator());
                    exportResult.append(System.lineSeparator());
                });
        }

        return exportResult.toString().trim();
    }
}
