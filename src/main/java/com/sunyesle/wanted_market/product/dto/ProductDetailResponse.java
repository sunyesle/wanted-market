package com.sunyesle.wanted_market.product.dto;

import com.sunyesle.wanted_market.global.enums.ProductStatus;
import com.sunyesle.wanted_market.product.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ProductDetailResponse {
    private final Long id;
    private final String name;
    private final Integer price;
    private final Integer reservedStock;
    private final Integer stock;
    private final ProductStatus status;
    private List<OfferInfo> offers;

    public void setOffers(List<OfferInfo> offers) {
        this.offers = offers;
    }

    @Builder
    private ProductDetailResponse(Long id, String name, Integer price, Integer reservedStock, Integer stock, ProductStatus status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.reservedStock = reservedStock;
        this.stock = stock;
        this.status = status;
        this.offers = Collections.emptyList();
    }

    public static ProductDetailResponse of(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .reservedStock(product.getReservedStock())
                .stock(product.getStock())
                .status(product.getStatus()).build();
    }
}
