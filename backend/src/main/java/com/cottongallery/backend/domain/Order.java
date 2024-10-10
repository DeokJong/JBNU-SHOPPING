package com.cottongallery.backend.domain;

import com.cottongallery.backend.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Address delivery;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(updatable = false, nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Order(Account account, Address delivery) {
        this.account = account;
        this.delivery = delivery;
        this.orderDate = LocalDate.now();
        this.status = OrderStatus.ORDER;
    }

    public static Order createOrder(Account account, Address delivery, Coupon coupon, OrderItem... orderItems) {
        Order order = new Order(account, delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        if (coupon != null) {
            order.coupon = coupon;
        }

        return order;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }
}
