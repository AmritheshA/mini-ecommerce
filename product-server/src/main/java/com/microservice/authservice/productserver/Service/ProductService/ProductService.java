package com.microservice.authservice.productserver.Service.ProductService;

import com.microservice.authservice.productserver.Entity.CartItemDto;
import com.microservice.authservice.productserver.Entity.CartProductResponseDto;
import com.microservice.authservice.productserver.Entity.ProductEntity;
import com.microservice.authservice.productserver.Entity.productRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ProductService {

    public ResponseEntity<?> getAllProducts();

    ResponseEntity<?> getProduct(Long productId);

    ResponseEntity<ProductEntity> editProductById(Long productId, productRequestDto details );

    ResponseEntity<ProductEntity> addProduct(productRequestDto product);

    ResponseEntity<String> deleteProductById(Long productId);

    Boolean productExistById(Long productId);

    ResponseEntity<List<CartProductResponseDto>> getCartProducts(Set<CartItemDto> items);
}
