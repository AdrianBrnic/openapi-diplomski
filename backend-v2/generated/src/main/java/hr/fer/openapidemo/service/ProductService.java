package hr.fer.openapidemo.service;

import hr.fer.openapidemo.entity.Product;
import hr.fer.openapidemo.model.CreateProductRequest;
import hr.fer.openapidemo.model.PageMetadata;
import hr.fer.openapidemo.model.ProductPage;
import hr.fer.openapidemo.model.ProductResponse;
import hr.fer.openapidemo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductPage getProducts(Integer page, Integer size, String sort,
                                   Double minPrice, Double maxPrice) {
        Sort sorting = Sort.by(Sort.Direction.ASC, "name");
        if (sort != null && sort.contains(",")) {
            String[] parts = sort.split(",");
            Sort.Direction direction = parts[1].equalsIgnoreCase("asc")
                    ? Sort.Direction.ASC : Sort.Direction.DESC;
            sorting = Sort.by(direction, parts[0]);
        }

        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20,
                sorting
        );

        Page<Product> productPage;
        if (minPrice != null && maxPrice != null) {
            productPage = productRepository.findByPriceBetween(
                    BigDecimal.valueOf(minPrice),
                    BigDecimal.valueOf(maxPrice),
                    pageable
            );
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<ProductResponse> content = productPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        PageMetadata metadata = new PageMetadata();
        metadata.setNumber(productPage.getNumber());
        metadata.setSize(productPage.getSize());
        metadata.setTotalElements(productPage.getTotalElements());
        metadata.setTotalPages(productPage.getTotalPages());

        ProductPage result = new ProductPage();
        result.setContent(content);
        result.setPage(metadata);
        return result;
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proizvod s ID-om " + id + " nije pronađen"));
        return toResponse(product);
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(BigDecimal.valueOf(request.getPrice()));
        product.setStock(request.getStock());
        productRepository.save(product);
        return toResponse(product);
    }

    public ProductResponse updateProduct(Long id, CreateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proizvod s ID-om " + id + " nije pronađen"));

        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(BigDecimal.valueOf(request.getPrice()));
        if (request.getStock() != null) product.setStock(request.getStock());

        productRepository.save(product);
        return toResponse(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Proizvod s ID-om " + id + " nije pronađen");
        }
        productRepository.deleteById(id);
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice().doubleValue());
        response.setStock(product.getStock());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}
