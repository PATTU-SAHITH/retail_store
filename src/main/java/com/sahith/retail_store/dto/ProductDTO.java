package com.sahith.retail_store.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductDTO {
    private Long id;
    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private String categoryName;
    private Integer stockQuantity;

    public ProductDTO() {}

    public ProductDTO(Long id, String name, String sku, String description, BigDecimal price, String categoryName, Integer stockQuantity) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.categoryName = categoryName;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public static ProductDTOBuilder builder() {
        return new ProductDTOBuilder();
    }

    public static class ProductDTOBuilder {
        private Long id;
        private String name;
        private String sku;
        private String description;
        private BigDecimal price;
        private String categoryName;
        private Integer stockQuantity;

        public ProductDTOBuilder id(Long id) { this.id = id; return this; }
        public ProductDTOBuilder name(String name) { this.name = name; return this; }
        public ProductDTOBuilder sku(String sku) { this.sku = sku; return this; }
        public ProductDTOBuilder description(String description) { this.description = description; return this; }
        public ProductDTOBuilder price(BigDecimal price) { this.price = price; return this; }
        public ProductDTOBuilder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public ProductDTOBuilder stockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; return this; }
        public ProductDTO build() { return new ProductDTO(id, name, sku, description, price, categoryName, stockQuantity); }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(sku, that.sku) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(categoryName, that.categoryName) && Objects.equals(stockQuantity, that.stockQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sku, description, price, categoryName, stockQuantity);
    }
}
