package com.example.demo.service;

import com.example.demo.domain.Product;
import com.example.demo.dto.ProductDTO;
import com.example.demo.repository.ProductRepository;
import com.example.demo.web.error.ConflictException;
import com.example.demo.web.error.NotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado: id=" + id));
    }

    @Transactional
    public Product create(ProductDTO dto) {
        if(repository.existsByNameIgnoreCase(dto.getName())) {
            throw new ConflictException("Já existe produto com esse nome.");
        }
        Product product = new Product(null, dto.getName(), dto.getPrice(), dto.getStcok());
        return repository.save(product);
    }

    @Transactional
    public Product update(Long id, ProductDTO dto) {
        Product product = findById(id);
        if(!product.getName().equalsIgnoreCase(dto.getName())
                && repository.existsByNameIgnoreCase(dto.getName())){
            throw new ConflictException("Já existe produto com este nome.");
        }
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStcok(dto.getStcok());
        return repository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = findById(id);
        repository.delete(product);
    }
}
