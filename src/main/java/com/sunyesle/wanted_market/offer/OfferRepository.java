package com.sunyesle.wanted_market.offer;

import com.sunyesle.wanted_market.global.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    boolean existsByProductIdAndBuyerIdAndStatus(Long productId, Long buyerId, OfferStatus offerStatus);

    List<Offer> findByBuyerId(Long memberId);

    @Query("select o from Offer o join Product p on p.id = o.productId and p.memberId = :memberId")
    List<Offer> findReceivedOffers(Long memberId);

    @Query("select o from Offer o join Product p on p.id = o.productId and p.id = :id and (p.memberId = :memberId or o.buyerId = :memberId)")
    List<Offer> findByProductIdAndMemberId(Long id, Long memberId);
}
