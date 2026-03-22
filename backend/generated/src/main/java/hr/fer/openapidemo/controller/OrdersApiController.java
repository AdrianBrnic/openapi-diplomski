package hr.fer.openapidemo.controller;

import hr.fer.openapidemo.model.CreateOrderRequest;
import hr.fer.openapidemo.model.OrderPage;
import hr.fer.openapidemo.model.OrderResponse;
import hr.fer.openapidemo.model.OrderStatus;
import hr.fer.openapidemo.model.UpdateOrderStatusRequest;
import hr.fer.openapidemo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrdersApiController implements OrdersApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderPage> getOrders(Integer page, Integer size, OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrders(page, size, status));
    }

    @Override
    public ResponseEntity<OrderResponse> createOrder(CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


}
