package com.cottongallery.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id", updatable = false, nullable = false)
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private Order order;

    @Column(updatable = false, nullable = false)
    private Integer orderPrice;

    @Column(updatable = false, nullable = false)
    private Integer discountPercent;

    @Column(updatable = false, nullable = false)
    private Integer count;

    private OrderItem(Item item, Integer orderPrice, Integer discountPercent, Integer count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.discountPercent = discountPercent;
        this.count = count;
    }

    public static OrderItem createOrderItem(Item item, Integer orderPrice, Integer count) {
        OrderItem orderItem = new OrderItem(item, orderPrice, item.getDiscount().getDiscountPercent(), count);

        item.reduceStockQuantity(count);

        return orderItem;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }
}
