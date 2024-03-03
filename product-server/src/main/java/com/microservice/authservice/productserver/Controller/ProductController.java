package com.microservice.authservice.productserver.Controller;

import com.microservice.authservice.productserver.Entity.ProductEntity;
import com.microservice.authservice.productserver.Service.ProductService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get-products")
    public ResponseEntity<?> getProducts(){
        return  productService.getAllProducts();
    }

    @GetMapping("/get-product/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId){
        return productService.getProduct(productId);
    }

}
