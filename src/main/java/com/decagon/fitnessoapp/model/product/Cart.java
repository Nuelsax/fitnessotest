package com.decagon.fitnessoapp.model.product;

import com.decagon.fitnessoapp.model.user.Person;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="product")
    @Column(name="quantity")
    @CollectionTable(name="intangible", joinColumns=@JoinColumn(name = "intangible_product_id"))
    private Map<String, Integer> intangibleProduct = new HashMap<String, Integer>();

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="product")
    @Column(name="quantity")
    @CollectionTable(name="tangible", joinColumns=@JoinColumn(name = "tangible_product_id"))
    private Map<String, Integer> tangibleProduct = new HashMap<String, Integer>();

   @OneToOne
   @JoinColumn(name = "person_id", referencedColumnName = "id")
   private Person person;

    //Todo: dont think this is neccessary
    @NotNull
    @Column(nullable = false)
    private Integer quantity;

}
