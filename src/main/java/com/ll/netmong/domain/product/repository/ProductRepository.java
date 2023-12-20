package com.ll.netmong.domain.product.repository;

import com.ll.netmong.domain.product.entity.Product;
import com.ll.netmong.domain.product.util.Category;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p join fetch p.image")
    List<Product> findAllWithImage();

    @Query("select p from Product p join fetch p.image where p.category = :category")
    List<Product> findByCategory(@Param("category") Category category);

    @Query("select p from Product p join fetch p.image where p.productName = :productName")
    List<Product> findByProductName(@Param("productName") String productName);

    @Query("select p from Product p left join fetch p.image")
    Page<Product> findAllPageWithImage(Pageable pageable);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Product s where s.id = :id")
    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);
}
