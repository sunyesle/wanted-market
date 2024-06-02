package com.sunyesle.wanted_market.controller;

import com.sunyesle.wanted_market.dto.ProductDetailResponse;
import com.sunyesle.wanted_market.dto.ProductRequest;
import com.sunyesle.wanted_market.dto.ProductResponse;
import com.sunyesle.wanted_market.security.CustomUserDetails;
import com.sunyesle.wanted_market.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> saveProduct(@AuthenticationPrincipal CustomUserDetails member, @RequestBody ProductRequest request){
        ProductResponse response = productService.save(request, member.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long id){
        ProductDetailResponse response = productService.getProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductDetailResponse>> getProducts(){
        List<ProductDetailResponse> response = productService.getProducts();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
