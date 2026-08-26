package com.rmsvg.livestock.service;

import com.rmsvg.livestock.domain.Enums.LivestockStatus;
import com.rmsvg.livestock.domain.Enums.OrderStatus;
import com.rmsvg.livestock.domain.Enums.PricingType;
import com.rmsvg.livestock.dto.OrderCreateRequest;
import com.rmsvg.livestock.entity.Customer;
import com.rmsvg.livestock.entity.CustomerOrder;
import com.rmsvg.livestock.entity.Livestock;
import com.rmsvg.livestock.entity.OrderItem;
import com.rmsvg.livestock.exception.ApiException;
import com.rmsvg.livestock.repository.CustomerRepository;
import com.rmsvg.livestock.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final LivestockService livestockService;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        LivestockService livestockService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.livestockService = livestockService;
    }

    @Transactional
    public Map<String, Object> create(OrderCreateRequest req) {
        Livestock livestock = livestockService.getEntity(req.livestockId());
        if (livestock.getPricingType() == PricingType.FIXED
                && livestock.getStatus() == LivestockStatus.SOLD) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "This animal has already been sold");
        }
        if (livestock.getPricingType() == PricingType.PER_KG) {
            if (livestock.getMinOrderQty() != null && req.quantity().compareTo(livestock.getMinOrderQty()) < 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Quantity is below the minimum order");
            }
            if (livestock.getAvailableQty() != null && req.quantity().compareTo(livestock.getAvailableQty()) > 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Not enough stock available");
            }
        }

        Customer customer = customerRepository.findByPhone(req.phone()).orElseGet(Customer::new);
        customer.setName(req.customerName());
        customer.setPhone(req.phone());
        customer.setEmail(req.email());
        customer.setLocation(req.deliveryAddress());
        customer = customerRepository.save(customer);

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setDeliveryAddress(req.deliveryAddress());
        order.setOrderStatus(OrderStatus.PENDING);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setLivestock(livestock);
        item.setQuantity(req.quantity());
        item.setPrice(livestock.getPrice());
        item.setProductName(livestock.getBreed() + " (" + livestock.getAnimalCode() + ")");
        order.getItems().add(item);
        order.setTotalAmount(livestock.getPrice().multiply(req.quantity()));

        return toMap(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> list(OrderStatus status) {
        List<CustomerOrder> orders = status == null ? orderRepository.findAll() : orderRepository.findByOrderStatus(status);
        return orders.stream().map(this::toMap).toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> get(Long id) {
        return toMap(orderRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Order not found")));
    }

    @Transactional
    public Map<String, Object> updateStatus(Long id, OrderStatus next) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Order not found"));
        OrderStatus prev = order.getOrderStatus();
        order.setOrderStatus(next);
        if (next == OrderStatus.PAYMENT_RECEIVED || next == OrderStatus.CONFIRMED) {
            order.setPaymentStatus(next == OrderStatus.PAYMENT_RECEIVED ? "PAID" : order.getPaymentStatus());
        }
        boolean becomingConfirmed = next == OrderStatus.CONFIRMED && prev != OrderStatus.CONFIRMED
                && prev != OrderStatus.PAYMENT_RECEIVED && prev != OrderStatus.READY
                && prev != OrderStatus.DELIVERED && prev != OrderStatus.COMPLETED;
        if (becomingConfirmed) {
            applyInventory(order);
        }
        return toMap(orderRepository.save(order));
    }

    private void applyInventory(CustomerOrder order) {
        for (OrderItem item : order.getItems()) {
            Livestock livestock = item.getLivestock();
            if (livestock == null) continue;
            if (livestock.getPricingType() == PricingType.FIXED) {
                if (livestock.getStatus() == LivestockStatus.SOLD) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Animal already sold: " + livestock.getAnimalCode());
                }
                livestock.setStatus(LivestockStatus.SOLD);
                livestock.setFeatured(false);
            } else {
                BigDecimal stock = livestock.getAvailableQty() == null ? BigDecimal.ZERO : livestock.getAvailableQty();
                if (stock.compareTo(item.getQuantity()) < 0) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Not enough chicken stock");
                }
                livestock.setAvailableQty(stock.subtract(item.getQuantity()));
            }
        }
    }

    public List<CustomerOrder> byCustomer(Long customerId) {
        return orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
    }

    public Map<String, Object> toMap(CustomerOrder o) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", o.getId());
        m.put("orderDate", o.getOrderDate());
        m.put("totalAmount", o.getTotalAmount());
        m.put("paymentStatus", o.getPaymentStatus());
        m.put("orderStatus", o.getOrderStatus());
        m.put("deliveryAddress", o.getDeliveryAddress());
        if (o.getCustomer() != null) {
            m.put("customerName", o.getCustomer().getName());
            m.put("phone", o.getCustomer().getPhone());
            m.put("customerId", o.getCustomer().getId());
        }
        m.put("items", o.getItems().stream().map(item -> {
            Map<String, Object> i = new HashMap<>();
            i.put("productName", item.getProductName());
            i.put("quantity", item.getQuantity());
            i.put("price", item.getPrice());
            if (item.getLivestock() != null) {
                i.put("livestockId", item.getLivestock().getId());
                i.put("category", item.getLivestock().getCategory());
            }
            return i;
        }).toList());
        return m;
    }
}
