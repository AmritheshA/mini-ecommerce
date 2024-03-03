package com.microservice.authservice.productserver.Service.ProductService;

import com.microservice.authservice.productserver.Entity.CartItemDto;
import com.microservice.authservice.productserver.Entity.CartProductResponseDto;
import com.microservice.authservice.productserver.Entity.ProductEntity;
import com.microservice.authservice.productserver.Entity.productRequestDto;
import com.microservice.authservice.productserver.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImp(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductEntity> allProducts = productRepository.findAll();
            return new ResponseEntity<>(allProducts, HttpStatus.OK);
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went Wrong while fetching Products From Database");
        }
    }

    @Override
    public ResponseEntity<?> getProduct(Long productId) {
        try {
            ProductEntity product = productRepository.findById(productId).get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with this id is not present");
        }
    }

    @Override
    public ResponseEntity<ProductEntity> editProductById(Long productId, productRequestDto requestDTO) {
        try {
            ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("Product not found"));

            Optional.ofNullable(requestDTO.getProductName()).ifPresent(product::setProductName);
            Optional.ofNullable(requestDTO.getProductPrise()).ifPresent(product::setProductPrise);
            Optional.ofNullable(requestDTO.getStock()).ifPresent(product::setStock);

            saveProduct(product);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("EditProductById . productServiceImp");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ProductEntity> addProduct(productRequestDto productDto) {
        try {
            ProductEntity product = new ProductEntity(productDto.getProductName(), productDto.getProductPrise(), productDto.getStock());

            return new ResponseEntity<>(saveProduct(product), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Something went wrong while adding product");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteProductById(Long productId) {

        try {
            if (!isProductExistById(productId))
                return new ResponseEntity<>("ProductId is not found", HttpStatus.NOT_FOUND);
            productRepository.deleteById(productId);

            return new ResponseEntity<>("Product Removed Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Boolean productExistById(Long productId) {

        try {
            return productRepository.existsById(productId);
        } catch (Exception e) {
            throw new NoSuchElementException("Product Not Found");
        }

    }

    @Override
    public ResponseEntity<List<CartProductResponseDto>> getCartProducts(Set<CartItemDto> items) {
        try {
            List<CartProductResponseDto> responseDto = new ArrayList<>();

            Set<Long> productIds = items.stream()
                    .map(CartItemDto::getProductId)
                    .collect(Collectors.toSet());

            List<ProductEntity> productList = productRepository.findByProductIdIn(productIds);

            for (CartItemDto item : items) {
                CartProductResponseDto cartItem = new CartProductResponseDto();
                cartItem.setProductId(item.getProductId());
                cartItem.setQuantity(item.getQuantity());

                productList.stream()
                        .filter(product -> product.getProductId().equals(item.getProductId()))
                        .findFirst()
                        .ifPresent(product -> {
                            cartItem.setProductName(product.getProductName());
                            cartItem.setProductPrise(product.getProductPrise());
                        });

                responseDto.add(cartItem);
            }

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch (Exception e){
            log.info("getCartProducts Error {}",e.getMessage());
            throw new RuntimeException("@getCartProducts : ProductServiceImp");
        }
    }


    private boolean isProductExistById(Long productId) {
        return productRepository.existsById(productId);
    }

    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }
}
