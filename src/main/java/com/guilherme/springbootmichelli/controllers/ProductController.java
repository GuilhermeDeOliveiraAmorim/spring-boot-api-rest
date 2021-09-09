package com.guilherme.springbootmichelli.controllers;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guilherme.springbootmichelli.models.ProductModel;
import com.guilherme.springbootmichelli.repositories.ProductRepository;

@RestController
public class ProductController {

	@Autowired
	ProductRepository productRepository;
	
	@GetMapping("/products")
	public ResponseEntity<List<ProductModel>> getAllProducts() {
		
		List<ProductModel> productsList = productRepository.findAll();
		
		if(productsList.isEmpty()) {
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		} else {
			
			for (ProductModel product : productsList) {
				
				long id = product.getId();
				
				product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
				
			}
			
			return new ResponseEntity<List<ProductModel>>(productsList, HttpStatus.OK);
			
		}
		
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductModel> getOneProduct(@PathVariable(value = "id") long id) {
		
		Optional<ProductModel> productO = productRepository.findById(id);
		
		if (!productO.isPresent()) {
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		} else {
			
			productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
			
			return new ResponseEntity<ProductModel>(productO.get(), HttpStatus.OK);
			
		}
		
	}
	
	@PostMapping("/products")
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Validated ProductModel product) {
		
		return new ResponseEntity<ProductModel>(productRepository.save(product), HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") long id) {
		
		Optional<ProductModel> productO = productRepository.findById(id);
		
		if (!productO.isPresent()) {
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		} else {
			
			productRepository.delete(productO.get());
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}
		
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<ProductModel> updateProduct(@PathVariable(value = "id") long id, @RequestBody @Validated ProductModel product) {
		
		Optional<ProductModel> productO = productRepository.findById(id);
		
		if (!productO.isPresent()) {
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		} else {

			product.setId(productO.get().getId());
			
			return new ResponseEntity<ProductModel>(productRepository.save(product), HttpStatus.OK);
			
		}
		
	}
	
}