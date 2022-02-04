package com.decagon.fitnessoapp.model.product;

import com.decagon.fitnessoapp.model.user.Person;
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
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @OneToMany(fetch = FetchType.LAZY)
   @JoinColumn(name = "intangible_product_id", referencedColumnName = "id")
    private List<IntangibleProduct> intangibleProducts;

   @OneToMany(fetch = FetchType.LAZY)
   @JoinColumn(name = "tangible_product_id", referencedColumnName = "id")
    private List<TangibleProduct> tangibleProducts;

   @OneToOne
   @JoinColumn(name = "person_id", referencedColumnName = "id")
   private Person person;

    //Todo: dont think this is neccessary
    @NotNull
    @Column(nullable = false)
    private Integer quantity;


}
