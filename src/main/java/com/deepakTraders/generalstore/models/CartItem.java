package com.deepakTraders.generalstore.models;

import com.deepakTraders.generalstore.repositories.ProductRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cartItems")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;

    private String size;
    private int quantity;
    private Integer price;
    private Integer discountedPrice;
    private Long userId;
}
