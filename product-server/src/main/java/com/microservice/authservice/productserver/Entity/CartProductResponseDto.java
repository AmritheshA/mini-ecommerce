package com.microservice.authservice.productserver.Entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductResponseDto {

    private Long productId;
    private String productName;
    private Double productPrise;
    private Long quantity;


}
