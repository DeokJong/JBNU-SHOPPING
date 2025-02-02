package com.cottongallery.backend.order.domain;

import com.cottongallery.backend.auth.domain.Account;
import com.cottongallery.backend.auth.domain.Address;

import com.cottongallery.backend.common.domain.base.BaseEntity;
import com.cottongallery.backend.order.exception.OrderAlreadyCompletedException;
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
public class Order extends BaseEntity {

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
    @JoinColumn(name = "coupon_id")
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

    public static Order createOrder(Account account, Address delivery, Coupon coupon, List<OrderItem> orderItems) {
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

    /** 주문 취소 */
    public void cancel() {
        if (status == OrderStatus.COMP) {
            throw new OrderAlreadyCompletedException();
        }

       changeOrderStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    /** 주문 상태 변경 */
    public void changeOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }
}
