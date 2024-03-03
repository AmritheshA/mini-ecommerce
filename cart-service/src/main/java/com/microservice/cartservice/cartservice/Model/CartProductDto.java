package com.microservice.cartservice.cartservice.Model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductDto {

    private Long productId;
    private String productName;
    private Double productPrise;
    private Long quantity;


}

