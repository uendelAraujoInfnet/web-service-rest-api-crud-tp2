package com.example.demo.web;

import com.example.demo.domain.Product;
import com.example.demo.dto.ProductDTO;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductDTO dto, UriComponentsBuilder uriBuilder) {
        Product productCreated = service.create(dto);
        URI location = uriBuilder.path("/api/v1/products/{id}").buildAndExpand(productCreated.getId()).toUri();
        return ResponseEntity.created(location).body(productCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(service.update(id, dto)); // 200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
