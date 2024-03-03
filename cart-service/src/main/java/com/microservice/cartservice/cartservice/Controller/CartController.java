package com.microservice.cartservice.cartservice.Controller;

import com.microservice.cartservice.cartservice.Service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/get-cart-product")
    public ResponseEntity<?> getCartProducts(@RequestParam Long userId, HttpServletRequest request) {
        return cartService.getCartProductsByUserId(userId, request);
    }

    @GetMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestParam("productId") Long productId,
                                       HttpServletRequest request) {
        return cartService.addToCart(productId, request);
    }

    @DeleteMapping("delete-cart-product")
    public ResponseEntity<?> deleteCartProduct(@RequestParam("productId") Long productId,
                                               @RequestParam("userId") Long userId,
                                               HttpServletRequest request) {
        return cartService.removeCartProduct(userId, productId, request);
    }

    @GetMapping("/product-quantity")
    public ResponseEntity<?> productQuantityIncOrDec(@RequestParam("productId") Long productId,
                                                     @RequestParam("userId") Long userId,
                                                     @RequestParam("flag") Integer flag,
                                                     HttpServletRequest request) {
        return cartService.productQuantityIncOrDec(userId, productId, flag, request);
    }
}
