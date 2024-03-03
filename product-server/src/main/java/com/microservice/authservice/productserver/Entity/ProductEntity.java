package com.microservice.authservice.productserver.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    @Column(name = "productId")
    private Long productId;
    private String productName;
    private Double productPrise;
    private Long stock;

    public ProductEntity(String productName, Double productPrise, Long stock) {
        this.productName = productName;
        this.productPrise = productPrise;
        this.stock = stock;
    }
}
