package com.guilherme.springbootmichelli.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guilherme.springbootmichelli.models.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

}
