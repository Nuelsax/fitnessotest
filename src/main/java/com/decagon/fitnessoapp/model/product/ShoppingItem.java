package com.decagon.fitnessoapp.model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

//@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ShoppingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private IntangibleProduct intangibleProducts;

    @OneToOne
    private TangibleProduct tangibleProducts;

    //Todo: dont think this is necessary
    @NotNull
    @Column(nullable = false)
    private Integer quantity;

    public ShoppingItem(IntangibleProduct inTangibleProducts, TangibleProduct tangibleProducts, Integer quantity) {
        this.intangibleProducts = inTangibleProducts;
        this.tangibleProducts = tangibleProducts;
        this.quantity = quantity;
    }
}
