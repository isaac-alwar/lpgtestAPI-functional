package com.example.lpgtest.controller;

import com.example.lpgtest.model.ProductCategory;
import com.example.lpgtest.repository.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@CrossOrigin(origins = { "http://localhost:3000" }) /* , maxAge = 600000*/
@RequestMapping("/api")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /* Find All */
    @GetMapping("/category")
    public Flux<ProductCategory> getAllCategories() {
        return categoryRepository.findAllCategories();
    }

    /* Find By Id */
    @GetMapping("/category/{id}")
    public Mono<ProductCategory> getCategoryById(@PathVariable("id") Long id) {
        return categoryRepository.findById(id);
    }

    /* Add New Category */
    public Publisher<ResponseEntity<ProductCategory>> addCategory(@RequestBody ProductCategory category) {
        return categoryRepository.addCategory(category)
                .map(categoryNew -> ResponseEntity.created(URI.create("/category"))
                .build());
    }

    /* Update Category */
    @PutMapping("/category/{id}")
    public Mono<ResponseEntity<ProductCategory>>
        updateCategory(@PathVariable("id") Long id, @RequestBody ProductCategory category ) {
        return categoryRepository.updateCategory(id, category)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound()
                        .build());
    }

    /* Delete Category */
    @DeleteMapping("/category/{id}")
    public Mono<Void> removeCategoryById(@PathVariable("id") Long id) {
        return categoryRepository.deleteCategoryById(id);
    }
}
