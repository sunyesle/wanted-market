package com.sunyesle.wanted_market.product;

import com.sunyesle.wanted_market.global.enums.ProductStatus;
import com.sunyesle.wanted_market.global.exception.ErrorCodeException;
import com.sunyesle.wanted_market.global.exception.ProductErrorCode;
import com.sunyesle.wanted_market.offer.Offer;
import com.sunyesle.wanted_market.offer.OfferRepository;
import com.sunyesle.wanted_market.product.dto.OfferInfo;
import com.sunyesle.wanted_market.product.dto.ProductDetailResponse;
import com.sunyesle.wanted_market.product.dto.ProductRequest;
import com.sunyesle.wanted_market.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;

    public ProductResponse save(ProductRequest request, Long memberId) {
        Product product = new Product(memberId, request.getName(), request.getPrice(), request.getQuantity());
        productRepository.save(product);
        return new ProductResponse(product.getId());
    }

    public ProductDetailResponse getProduct(Long id, Long memberId) {
        Product product = findById(id);
        List<Offer> offers = offerRepository.findByProductIdAndMemberId(id, memberId);

        ProductDetailResponse productDetailResponse = ProductDetailResponse.of(product);
        productDetailResponse.setOffers(offers.stream().map(OfferInfo::of).toList());
        return productDetailResponse;
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ErrorCodeException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    public List<ProductDetailResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductDetailResponse::of).toList();
    }

    public void makeReservation(Long productId, Integer quantity) {
        // 예약
        Product product = productRepository.findByIdWithPessimisticLock(productId);
        product.reserve(quantity);
    }

    public void placeOrder(Long productId, Integer quantity) {
        // 주문 확정
        Product product = productRepository.findByIdWithPessimisticLock(productId);
        product.purchase(quantity);
    }

    public boolean checkSeller(Long productId, Long memberId) {
        Product product = findById(productId);
        return memberId.equals(product.getMemberId());
    }

    public boolean checkAvailability(Long id) {
        Product product = findById(id);
        return product.getStatus() == ProductStatus.AVAILABLE;
    }
}
