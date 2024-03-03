package com.microservice.authservice.productserver.Controller;

import com.microservice.authservice.productserver.Entity.CartItemDto;
import com.microservice.authservice.productserver.Entity.CartProductResponseDto;
import com.microservice.authservice.productserver.Service.ProductService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class FeignController {

    private final ProductService productService;

    @Autowired
    public FeignController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/v1/product/product-is-exist/{productId}")
    public Boolean verifyProductExistById(@PathVariable Long productId) {
        return productService.productExistById(productId);
    }

    @PostMapping("/api/v1/product/get-cart-products")
    public ResponseEntity<List<CartProductResponseDto>> getCartProducts(@RequestBody Set<CartItemDto> items){
        return productService.getCartProducts(items);
    }

}
