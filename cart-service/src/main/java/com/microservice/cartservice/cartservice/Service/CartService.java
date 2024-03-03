package com.microservice.cartservice.cartservice.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    ResponseEntity<?> addToCart(Long productId, HttpServletRequest request);

    ResponseEntity<?> getCartProductsByUserId(Long userId, HttpServletRequest request);

    ResponseEntity<?> removeCartProduct(Long userId, Long productId, HttpServletRequest request);

    ResponseEntity<?> productQuantityIncOrDec(Long userId, Long productId, Integer flag, HttpServletRequest request);

}
