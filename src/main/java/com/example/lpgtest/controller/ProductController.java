package com.example.lpgtest.controller;

import com.example.lpgtest.model.Product;
import com.example.lpgtest.repository.ProductsRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@CrossOrigin(origins = { "http://localhost:3000" }) /* , maxAge = 600000*/
@RequestMapping("/api")
public class ProductController {
    private final ProductsRepository productsRepository;

    public ProductController(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    /* Find All Products */
    @GetMapping("/products")
    public Flux<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    /* Find By Id */
    @GetMapping("/products/{id}")
    public Mono<Product> getProductById(@PathVariable("id") Long id)  {
        return productsRepository.findById(id);
    }

    /* Add New Product */
    @PostMapping("/products")
    public Publisher<ResponseEntity<Product>> addProduct(@RequestBody Product product) {
        return productsRepository.addProduct(product)
                .map(productNew -> ResponseEntity.created(URI.create("/products"))
                .build());
    }

    /* Update Product */
    @PutMapping("/products/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        return productsRepository.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound()
                .build());
    }

    /* Delete Product */
    @DeleteMapping(path = "/products/{id}")
    public Mono<Void> removeProductById(@PathVariable("id") Long id) {
        return productsRepository.deleteById(id);
    }

}
