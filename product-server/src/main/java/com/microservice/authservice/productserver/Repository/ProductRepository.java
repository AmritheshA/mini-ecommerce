package com.microservice.authservice.productserver.Repository;

import com.microservice.authservice.productserver.Entity.ProductEntity;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {

    public List<ProductEntity> findByProductIdIn(Set<Long> productIds);

}
