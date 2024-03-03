package com.microservice.authservice.productserver.Controller;

import com.microservice.authservice.productserver.Entity.ProductEntity;
import com.microservice.authservice.productserver.Entity.productRequestDto;
import com.microservice.authservice.productserver.Service.ProductService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/product")
public class AdminController {


    private final ProductService productService;

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add-product")
    public ResponseEntity<ProductEntity> addProduct(@RequestBody productRequestDto product) {
        return productService.addProduct(product);
    }

    @PostMapping("/edit-product/{productId}")
    public ResponseEntity<ProductEntity> editProductById(@PathVariable Long productId, @RequestBody productRequestDto requestData) {
        return productService.editProductById(productId, requestData);
    }

    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long productId) {
        return productService.deleteProductById(productId);
    }

}
