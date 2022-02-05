package com.decagon.fitnessoapp.model.user;

import com.decagon.fitnessoapp.model.product.IntangibleProduct;
import com.decagon.fitnessoapp.model.product.Product;
import com.decagon.fitnessoapp.model.product.TangibleProduct;
import lombok.*;

import javax.persistence.*;
import java.util.List;



@NoArgsConstructor
@Data
@Entity
public class Favourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "Person_id", referencedColumnName = "id")
    private Person person;
//
//    @ManyToOne
//    @JoinColumn(name = "TangibleProduct_id", referencedColumnName = "id")
//    private TangibleProduct tangibleProductList;
//
//    @ManyToOne
//    @JoinColumn(name = "IntangibleProduct_id", referencedColumnName = "id")
//    private IntangibleProduct intangibleProduct;

    @Column(nullable = false)
    private Long productId;


//    @Override
//    public String toString() {
//        return "Favourite{" +
//                "id=" + id +
////                ", personList=" + personList +
////                ", tangibleProductList=" + tangibleProductList +
////                ", intangibleProductList=" + intangibleProductList +
//                '}';
//    }
}
