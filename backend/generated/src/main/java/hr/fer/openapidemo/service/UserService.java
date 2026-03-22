package hr.fer.openapidemo.service;

import hr.fer.openapidemo.entity.User;
import hr.fer.openapidemo.model.UpdateUserRequest;
import hr.fer.openapidemo.model.UserPage;
import hr.fer.openapidemo.model.UserResponse;
import hr.fer.openapidemo.model.PageMetadata;
import hr.fer.openapidemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserPage getUsers(Integer page, Integer size, String sort) {
        Sort sorting = Sort.by(Sort.Direction.DESC, "createdAt");
        if (sort != null && sort.contains(",")) {
            String[] parts = sort.split(",");
            String field = parts[0];
            Sort.Direction direction = parts[1].equalsIgnoreCase("asc")
                    ? Sort.Direction.ASC : Sort.Direction.DESC;
            sorting = Sort.by(direction, field);
        }

        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20,
                sorting
        );

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponse> content = userPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        PageMetadata metadata = new PageMetadata();
        metadata.setNumber(userPage.getNumber());
        metadata.setSize(userPage.getSize());
        metadata.setTotalElements(userPage.getTotalElements());
        metadata.setTotalPages(userPage.getTotalPages());

        UserPage result = new UserPage();
        result.setContent(content);
        result.setPage(metadata);
        return result;
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik s ID-om " + id + " nije pronađen"));
        return toResponse(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik s ID-om " + id + " nije pronađen"));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getBirthday() != null) user.setBirthday(request.getBirthday());

        userRepository.save(user);
        return toResponse(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Korisnik s ID-om " + id + " nije pronađen");
        }
        userRepository.deleteById(id);
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setBirthday(user.getBirthday());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
