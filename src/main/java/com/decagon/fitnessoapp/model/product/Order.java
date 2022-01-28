package com.decagon.fitnessoapp.model.product;

import com.decagon.fitnessoapp.model.user.Address;
import com.decagon.fitnessoapp.model.user.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person person;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "shoppingCart_id", referencedColumnName = "id", nullable = false)
    private List<ShoppingItem> shoppingItems;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @OneToOne
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id", nullable = false)
    private Address shippingAddress;

    @CreationTimestamp
    private Timestamp orderDate;

    @Column(name = "order_status")
    private String orderStatus;

    @OneToOne
    private CouponCode couponCode;

    @Column(nullable = false)
    private SHIPPING_METHOD shippingMethod;
}
