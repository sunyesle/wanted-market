package com.sunyesle.wanted_market.product.dto;

import com.sunyesle.wanted_market.global.enums.OfferStatus;
import com.sunyesle.wanted_market.offer.Offer;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OfferInfo {
    private final Long id;
    private final Long buyerId;
    private final Integer price;
    private final Integer quantity;
    private final OfferStatus status;

    @Builder
    public OfferInfo(Long id, Long buyerId, Integer price, Integer quantity, OfferStatus status) {
        this.id = id;
        this.buyerId = buyerId;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public static OfferInfo of(Offer offer) {
        return OfferInfo.builder()
                .id(offer.getId())
                .buyerId(offer.getBuyerId())
                .price(offer.getPrice())
                .quantity(offer.getQuantity())
                .status(offer.getStatus()).build();
    }
}
