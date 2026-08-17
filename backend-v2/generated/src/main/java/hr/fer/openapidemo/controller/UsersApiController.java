package hr.fer.openapidemo.controller;

import hr.fer.openapidemo.model.UpdateUserRequest;
import hr.fer.openapidemo.model.UserPage;
import hr.fer.openapidemo.model.UserResponse;
import hr.fer.openapidemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import hr.fer.openapidemo.model.OrderPage;
import hr.fer.openapidemo.service.OrderService;
import hr.fer.openapidemo.model.ChangePasswordRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
public class UsersApiController implements UsersApi {

    private final UserService userService;
    private final OrderService orderService;


    @Override
    public ResponseEntity<UserPage> getUsers(Integer page, Integer size, String sort) {
        return ResponseEntity.ok(userService.getUsers(page, size, sort));
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(Long id, UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @Override
    public ResponseEntity<OrderPage> getOrdersByUserId(Long id, Integer page, Integer size) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(id, page, size));
    }
	@PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.changePassword(id, request, email);
        return ResponseEntity.noContent().build();
    }
}
