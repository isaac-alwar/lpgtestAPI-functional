package com.example.lpgtest.repository;

import com.example.lpgtest.model.Product;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class ProductsRepository {
    private static final String KEY = "products";
    private final ReactiveRedisTemplate<byte[], byte[]> rxProductsTemplate;

    public ProductsRepository(ReactiveRedisTemplate<byte[], byte[]> rxProductsTemplate) {
        this.rxProductsTemplate = rxProductsTemplate;
    }

    public ReactiveHashOperations<byte[], byte[], Product> rxProductOps() {
        return this.rxProductsTemplate.opsForHash();
    }

    /* Find All */
    public Flux<Product> findAll() {
        return rxProductOps().values(KEY.getBytes());
    }

    /*Find By Id */
    public Mono<Product> findById(Long id) {
        return rxProductOps().get(KEY.getBytes(), id.toString().getBytes());
    }

    /* Save Product */
    public Mono<Product> addProduct(Product product) {
        product.setCreatedOn(Timestamp.from(Instant.now()));
        return rxProductOps().put(KEY.getBytes(), product.getId().toString().getBytes(),product)
                .log()
                .map(prod -> product);
    }

    /* Update Product */
    public Mono<Product> updateProduct(Long id, Product product) {
        product.setId(id);
        product.setUpdatedOn(Timestamp.from(Instant.now()));
        return rxProductOps().put(KEY.getBytes(), product.getId().toString().getBytes(), product)
                .log()
                .map(upp -> product);
    }

    /* Delete Product */
    public Mono<Void> deleteById(Long id) {
        return rxProductOps().remove(KEY.getBytes(), id)
                .flatMap(d -> Mono.empty());
    }
}
