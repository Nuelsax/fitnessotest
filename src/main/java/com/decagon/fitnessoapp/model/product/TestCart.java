package com.decagon.fitnessoapp.model.product;

import com.decagon.fitnessoapp.model.user.Person;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
//@Builder
public class TestCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uniqueCartId;

    private Long productId;

    private Integer quantity;

    private Long personId;

    private String cartReference;
}
