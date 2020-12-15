package com.example.lpgtest.repository;

import com.example.lpgtest.model.ProductCategory;
import jdk.jfr.Category;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class CategoryRepository {
    private static final String KEY = "categories";
    private final ReactiveRedisTemplate<byte[], byte[]> rxCategoryTemplate;

    public CategoryRepository(ReactiveRedisTemplate<byte[], byte[]> rxCategoryTemplate) {
        this.rxCategoryTemplate = rxCategoryTemplate;
    }

    public ReactiveHashOperations<byte[], byte[], ProductCategory> rxCategoryOps() {
        return rxCategoryTemplate.opsForHash();
    }

    /* Find All */
    public Flux<ProductCategory> findAllCategories() {
        return rxCategoryOps().values(KEY.getBytes());
    }

    /*Find By Id */
    public Mono<ProductCategory> findById(Long id) {
        return rxCategoryOps().get(KEY.getBytes(), id);
    }

    /* Save Category */
    public Mono<ProductCategory> addCategory(ProductCategory category) {
        return rxCategoryOps().put(KEY.getBytes(), category.getId().toString().getBytes(), category)
                .log()
                .map(cat -> category);
    }

    /* Update Category */
    public Mono<ProductCategory> updateCategory(Long id, ProductCategory category) {
        category.setId(id);
        return rxCategoryOps().put(KEY.getBytes(), category.getId().toString().getBytes(), category)
                .log()
                .map(upc -> category);
    }

    /* Delete Category */
    public Mono<Void> deleteCategoryById(Long id) {
        return rxCategoryOps().remove(KEY.getBytes(), id)
                .flatMap(del -> Mono.empty());
    }

}
