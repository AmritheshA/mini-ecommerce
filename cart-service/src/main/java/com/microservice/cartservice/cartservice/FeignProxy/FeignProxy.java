package com.microservice.cartservice.cartservice.FeignProxy;

import com.microservice.cartservice.cartservice.Model.CartEntity;
import com.microservice.cartservice.cartservice.Model.CartItem;
import com.microservice.cartservice.cartservice.Model.CartProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "product-service")
public interface FeignProxy {

    @GetMapping("/api/v1/product/product-is-exist/{productId}")
    public Boolean verifyProductExistById(@PathVariable Long productId,
                                          @RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/api/v1/product/get-cart-products")
    ResponseEntity<List<CartProductDto>> getCartProductsDetails(@RequestBody Set<CartItem> cartItem,
                                                                @RequestHeader("Authorization") String authorizationHeader);

}
