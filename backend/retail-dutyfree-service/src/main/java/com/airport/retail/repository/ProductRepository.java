package com.airport.retail.repository;

import com.airport.retail.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrueOrderByNameAsc();
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
}
