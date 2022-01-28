package com.decagon.fitnessoapp.model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CouponCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    @NotNull
    @Column(name = "discount_percent", nullable = false)
    private float discountPercent;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;
}
