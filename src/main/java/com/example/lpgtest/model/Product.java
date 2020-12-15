package com.example.lpgtest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
@RedisHash("products")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Product implements Serializable {
    @Id
    private Long id;

    private String name;
    private String description;

    private String categoryId;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd HH:mm:ss z", locale = "en_GB")
    @JsonProperty("createdOn")
    private Timestamp createdOn;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd HH:mm:ss z", locale = "en_GB")
    @JsonProperty("updatedOn")
    private Timestamp updatedOn;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd HH:mm:ss z", locale = "en_GB")
    @JsonProperty("lastPurchaseDate")
    private Timestamp lastPurchaseDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Timestamp getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(Timestamp lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }
}
