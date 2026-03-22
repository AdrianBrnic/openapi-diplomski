package hr.fer.openapidemo.service;

import hr.fer.openapidemo.entity.Order;
import hr.fer.openapidemo.entity.OrderItem;
import hr.fer.openapidemo.entity.OrderStatus;
import hr.fer.openapidemo.entity.Product;
import hr.fer.openapidemo.entity.User;
import hr.fer.openapidemo.model.CreateOrderRequest;
import hr.fer.openapidemo.model.OrderItemResponse;
import hr.fer.openapidemo.model.OrderPage;
import hr.fer.openapidemo.model.OrderResponse;
import hr.fer.openapidemo.model.PageMetadata;
import hr.fer.openapidemo.model.UpdateOrderStatusRequest;
import hr.fer.openapidemo.repository.OrderRepository;
import hr.fer.openapidemo.repository.ProductRepository;
import hr.fer.openapidemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderPage getOrders(Integer page, Integer size,
                                hr.fer.openapidemo.model.OrderStatus status) {
        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Order> orderPage;
        if (status != null) {
            OrderStatus entityStatus = OrderStatus.valueOf(status.getValue());
            orderPage = orderRepository.findByStatus(entityStatus, pageable);
        } else {
            orderPage = orderRepository.findAll(pageable);
        }

        return toPage(orderPage);
    }

    public OrderPage getOrdersByUserId(Long userId, Integer page, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Korisnik s ID-om " + userId + " nije pronađen");
        }

        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return toPage(orderRepository.findByUserId(userId, pageable));
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Korisnik s ID-om " + request.getUserId() + " nije pronađen"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);

        BigDecimal total = BigDecimal.ZERO;

        for (var itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Proizvod s ID-om " + itemRequest.getProductId() + " nije pronađen"));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Nedovoljno zaliha za proizvod: " + product.getName());
            }

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(product.getPrice());

            order.getItems().add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setTotalPrice(total);
        orderRepository.save(order);
        return toResponse(order);
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Narudžba s ID-om " + id + " nije pronađena"));
        return toResponse(order);
    }

    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Narudžba s ID-om " + id + " nije pronađena"));

        order.setStatus(OrderStatus.valueOf(request.getStatus().getValue()));
        orderRepository.save(order);
        return toResponse(order);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Narudžba s ID-om " + id + " nije pronađena");
        }
        orderRepository.deleteById(id);
    }

    private OrderPage toPage(Page<Order> orderPage) {
        List<OrderResponse> content = orderPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        PageMetadata metadata = new PageMetadata();
        metadata.setNumber(orderPage.getNumber());
        metadata.setSize(orderPage.getSize());
        metadata.setTotalElements(orderPage.getTotalElements());
        metadata.setTotalPages(orderPage.getTotalPages());

        OrderPage result = new OrderPage();
        result.setContent(content);
        result.setPage(metadata);
        return result;
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setUnitPrice(item.getUnitPrice().doubleValue());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setSubtotal(
                item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())).doubleValue()
            );
            return itemResponse;
        }).toList();

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setItems(items);
        response.setTotalPrice(order.getTotalPrice().doubleValue());
        response.setStatus(hr.fer.openapidemo.model.OrderStatus.fromValue(order.getStatus().name()));
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
}
