package com.rmsvg.livestock.service;

import com.rmsvg.livestock.domain.Enums.EnquiryStatus;
import com.rmsvg.livestock.domain.Enums.LivestockStatus;
import com.rmsvg.livestock.domain.Enums.OrderStatus;
import com.rmsvg.livestock.domain.Enums.WholesaleStatus;
import com.rmsvg.livestock.entity.Customer;
import com.rmsvg.livestock.entity.CustomerOrder;
import com.rmsvg.livestock.entity.OrderItem;
import com.rmsvg.livestock.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final LivestockRepository livestockRepository;
    private final OrderRepository orderRepository;
    private final EnquiryRepository enquiryRepository;
    private final WholesaleRequestRepository wholesaleRequestRepository;
    private final CustomerRepository customerRepository;

    public DashboardService(LivestockRepository livestockRepository,
                            OrderRepository orderRepository,
                            EnquiryRepository enquiryRepository,
                            WholesaleRequestRepository wholesaleRequestRepository,
                            CustomerRepository customerRepository) {
        this.livestockRepository = livestockRepository;
        this.orderRepository = orderRepository;
        this.enquiryRepository = enquiryRepository;
        this.wholesaleRequestRepository = wholesaleRequestRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> stats() {
        Map<String, Object> m = new HashMap<>();
        m.put("totalLivestock", livestockRepository.count());
        m.put("available", livestockRepository.countByStatus(LivestockStatus.AVAILABLE));
        m.put("sold", livestockRepository.countByStatus(LivestockStatus.SOLD));
        m.put("reserved", livestockRepository.countByStatus(LivestockStatus.RESERVED));
        m.put("pendingOrders", orderRepository.countByOrderStatus(OrderStatus.PENDING));
        m.put("wholesaleRequests", wholesaleRequestRepository.countByStatus(WholesaleStatus.NEW));
        m.put("newEnquiries", enquiryRepository.countByStatus(EnquiryStatus.NEW));
        m.put("customers", customerRepository.count());
        List<Map<String, Object>> recent = orderRepository.findAll().stream()
                .sorted((a, b) -> b.getOrderDate().compareTo(a.getOrderDate()))
                .limit(6)
                .map(o -> {
                    Map<String, Object> r = new HashMap<>();
                    r.put("id", o.getId());
                    r.put("customerName", o.getCustomer() != null ? o.getCustomer().getName() : "");
                    r.put("totalAmount", o.getTotalAmount());
                    r.put("orderStatus", o.getOrderStatus());
                    String product = o.getItems().isEmpty() ? "" : o.getItems().get(0).getProductName();
                    r.put("product", product);
                    return r;
                }).toList();
        m.put("recentOrders", recent);
        return m;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> reports() {
        List<CustomerOrder> completed = orderRepository.findAll().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED || o.getOrderStatus() == OrderStatus.DELIVERED)
                .toList();
        BigDecimal sales = completed.stream().map(CustomerOrder::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        long goats = 0, cows = 0;
        BigDecimal chickenKg = BigDecimal.ZERO;
        for (CustomerOrder o : completed) {
            for (OrderItem item : o.getItems()) {
                if (item.getLivestock() == null) continue;
                switch (item.getLivestock().getCategory()) {
                    case GOAT -> goats += item.getQuantity().longValue();
                    case COW -> cows += item.getQuantity().longValue();
                    case CHICKEN -> chickenKg = chickenKg.add(item.getQuantity());
                }
            }
        }
        YearMonth now = YearMonth.now();
        BigDecimal monthly = completed.stream()
                .filter(o -> YearMonth.from(o.getOrderDate().atZone(ZoneId.systemDefault())).equals(now))
                .map(CustomerOrder::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> m = new HashMap<>();
        m.put("totalSales", sales);
        m.put("completedOrders", completed.size());
        m.put("goatsSold", goats);
        m.put("cowsSold", cows);
        m.put("chickenKg", chickenKg);
        m.put("month", now.toString());
        m.put("monthlySales", monthly);
        return m;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> customers() {
        return customerRepository.findAll().stream().map(this::customerSummary).toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> customer(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new com.rmsvg.livestock.exception.ApiException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Customer not found"));
        Map<String, Object> m = customerSummary(c);
        m.put("email", c.getEmail());
        m.put("location", c.getLocation());
        m.put("orders", orderRepository.findByCustomerIdOrderByOrderDateDesc(id).stream().map(o -> {
            Map<String, Object> r = new HashMap<>();
            r.put("id", o.getId());
            r.put("totalAmount", o.getTotalAmount());
            r.put("orderStatus", o.getOrderStatus());
            r.put("orderDate", o.getOrderDate());
            return r;
        }).toList());
        m.put("enquiries", enquiryRepository.findByCustomerIdOrderByCreatedAtDesc(id).stream().map(e -> {
            Map<String, Object> r = new HashMap<>();
            r.put("id", e.getId());
            r.put("product", e.getProductSnapshot());
            r.put("status", e.getStatus());
            r.put("createdAt", e.getCreatedAt());
            return r;
        }).toList());
        return m;
    }

    private Map<String, Object> customerSummary(Customer c) {
        List<CustomerOrder> orders = orderRepository.findByCustomerIdOrderByOrderDateDesc(c.getId());
        BigDecimal total = orders.stream().map(CustomerOrder::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Object> m = new HashMap<>();
        m.put("id", c.getId());
        m.put("name", c.getName());
        m.put("phone", c.getPhone());
        m.put("orderCount", orders.size());
        m.put("totalAmount", total);
        m.put("status", orders.isEmpty() ? "New" : "Active");
        return m;
    }
}
