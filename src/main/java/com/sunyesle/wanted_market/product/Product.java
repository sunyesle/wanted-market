package com.sunyesle.wanted_market.product;

import com.sunyesle.wanted_market.global.enums.ProductStatus;
import com.sunyesle.wanted_market.global.exception.ErrorCodeException;
import com.sunyesle.wanted_market.global.exception.ProductErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String name;

    private Integer price;

    private Integer reservedStock;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public Product(Long memberId, String name, Integer price, Integer stock) {
        this.memberId = memberId;
        this.name = name;
        this.price = price;
        this.reservedStock = 0;
        this.stock = stock;
        this.status = ProductStatus.AVAILABLE;
    }

    public void reserve(Integer quantity) {
        if (this.stock < 0) {
            throw new ErrorCodeException(ProductErrorCode.OUT_OF_STOCK);
        }
        this.reservedStock += quantity;
        this.stock -= quantity;
        updateStatus();
    }

    public void purchase(Integer quantity) {
        this.reservedStock -= quantity;
        updateStatus();
    }

    private void updateStatus() {
        if (this.stock == 0 && this.reservedStock == 0) {
            this.status = ProductStatus.COMPLETED; // 재고와 예약된 재고가 모두 0인 경우
        } else if (this.stock == 0) {
            this.status = ProductStatus.RESERVED; // 재고는 0이지만 예약된 재고가 있는 경우
        } else {
            this.status = ProductStatus.AVAILABLE; // 재고가 있는 경우
        }
    }
}
