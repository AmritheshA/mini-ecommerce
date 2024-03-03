package com.microservice.authservice.productserver.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class productRequestDto {
    private String productName;
    private Double productPrise;
    private Long stock;
}
