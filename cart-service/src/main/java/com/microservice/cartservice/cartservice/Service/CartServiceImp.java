package com.microservice.cartservice.cartservice.Service;

import com.microservice.cartservice.cartservice.FeignProxy.FeignProxy;
import com.microservice.cartservice.cartservice.Model.CartEntity;
import com.microservice.cartservice.cartservice.Model.CartItem;
import com.microservice.cartservice.cartservice.Model.CartProductDto;
import com.microservice.cartservice.cartservice.Repository.CartRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Slf4j
public class CartServiceImp implements CartService {

    private final CartRepository cartRepository;
    private final FeignProxy feignProxy;

    @Autowired
    public CartServiceImp(CartRepository cartRepository, FeignProxy feignProxy) {
        this.cartRepository = cartRepository;
        this.feignProxy = feignProxy;
    }


    @Override
    public ResponseEntity<?> getCartProductsByUserId(Long userId, HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        log.info("Token {}", token);
        log.info("User id {}", userId);
        if (token == null || userId == null)
            return new ResponseEntity<>("Authorization Header Is Null / User Id Is Null", HttpStatus.UNAUTHORIZED);

        try {
            CartEntity cart = cartRepository.findByUserId(userId);
            Set<CartItem> cartItem = cart.getItems();

            List<CartProductDto> cartProducts = feignProxy.getCartProductsDetails(cartItem, token).getBody();
            return new ResponseEntity<>(cartProducts, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error Message {}", e.getMessage());
            throw new RuntimeException("Something went wrong @CartServiceImp");
        }

    }

    @Override
    public ResponseEntity<?> addToCart(Long productId, HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        if (token == null) return new ResponseEntity<>("Authorization Header Is Null", HttpStatus.UNAUTHORIZED);

        if (Boolean.FALSE.equals(feignProxy.verifyProductExistById(productId, token))) {
            return new ResponseEntity<>("Product Id Is Failed To Find.", HttpStatus.NOT_FOUND);
        }
        try {

            Long userId = Long.valueOf(request.getHeader("userId"));
            log.info("User Id From the authenticated Request {}", userId);

            CartEntity cart = getCartByUserId(userId);
            CartItem item = cart.getItems().stream().filter(product -> product.getProductId().equals(productId)).findFirst().orElseGet(() -> {
                CartItem newItem = new CartItem(0L, productId);
                cart.getItems().add(newItem);
                return newItem;
            });
            item.setQuantity(item.getQuantity() + 1);
            cartRepository.save(cart);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("An error occurred while adding the product to the cart.", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<>("Product Added Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeCartProduct(Long userId, Long productId, HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        if (token == null) return new ResponseEntity<>("Authorization Header Is Null", HttpStatus.UNAUTHORIZED);

        if (Boolean.FALSE.equals(feignProxy.verifyProductExistById(productId, token))) {
            return new ResponseEntity<>("Product Id Is Failed To Find.", HttpStatus.NOT_FOUND);
        }

        try {
            CartEntity cart = cartRepository.findByUserId(userId);
            Set<CartItem> cartItems = cart.getItems();
            if (!cartItems.stream().anyMatch(item -> item.getProductId().equals(productId))) {
                return new ResponseEntity<>("Product With This Id Not Found", HttpStatus.NOT_FOUND);
            }
            cartItems.removeIf(cartItem -> cartItem.getProductId().equals(productId));
            cartRepository.save(cart);
            return new ResponseEntity<>("Successfully product removed", HttpStatus.OK);
        } catch (Exception e) {
            log.info("removeCartProduct {}", e.getMessage());
            throw new NoSuchElementException("@removeCartProduct in CartServiceImp");
        }
    }

    @Override
    public ResponseEntity<?> productQuantityIncOrDec(Long userId, Long productId, Integer flag, HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        if (token == null) return new ResponseEntity<>("Authorization Header Is Null", HttpStatus.UNAUTHORIZED);

        if (Boolean.FALSE.equals(feignProxy.verifyProductExistById(productId, token))) {
            return new ResponseEntity<>("Product Id Is Failed To Find.", HttpStatus.NOT_FOUND);
        }

        try {
            CartEntity cart = cartRepository.findByUserId(userId);
            CartItem filteredCart = cart.getItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst().get();
            if (flag.equals(1)) {
                filteredCart.setQuantity(filteredCart.getQuantity() + 1);
            } else {
                filteredCart.setQuantity(filteredCart.getQuantity() - 1);
            }
            cartRepository.save(cart);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("ProductQuantityIncOrDec");
        }


    }

    private CartEntity getCartByUserId(Long userId) {
        CartEntity cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new CartEntity();
            cart.setUserId(userId);
        }
        return cart;
    }
}
