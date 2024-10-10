package com.cottongallery.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;

    private Item(String name, Integer price, Integer stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public static Item createItem(String name, Integer price, Integer stockQuantity) {
        return new Item(name, price, stockQuantity);
    }

    public void reduceStockQuantity(Integer quantity) {
        stockQuantity = stockQuantity - quantity;
    }
}
