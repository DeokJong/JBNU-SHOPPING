package com.cottongallery.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer minimumOrderAmount;

    @Column(nullable = false)
    private Integer discountAmount;

    private Coupon(Account account, String name, LocalDate startDate, LocalDate endDate,
                   Integer minimumOrderAmount, Integer discountAmount) {
        this.account = account;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minimumOrderAmount = minimumOrderAmount;
        this.discountAmount = discountAmount;
    }

    public static Coupon createCoupon(Account account, String name, LocalDate startDate, LocalDate endDate,
                                      Integer minimumOrderAmount, Integer discountAmount) {
        return new Coupon(account, name, startDate, endDate, minimumOrderAmount, discountAmount);
    }
}
