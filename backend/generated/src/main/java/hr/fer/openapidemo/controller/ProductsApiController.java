package hr.fer.openapidemo.controller;

import hr.fer.openapidemo.model.CreateProductRequest;
import hr.fer.openapidemo.model.ProductPage;
import hr.fer.openapidemo.model.ProductResponse;
import hr.fer.openapidemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductsApiController implements ProductsApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductPage> getProducts(Integer page, Integer size, String sort,
                                                    Double minPrice, Double maxPrice) {
        return ResponseEntity.ok(productService.getProducts(page, size, sort, minPrice, maxPrice));
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Override
    public ResponseEntity<ProductResponse> createProduct(CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(Long id, CreateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
