package com.sunyesle.wanted_market.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.wanted_market.product.dto.ProductDetailResponse;
import com.sunyesle.wanted_market.product.dto.ProductRequest;
import com.sunyesle.wanted_market.global.enums.ProductStatus;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductSteps {

    @SneakyThrows
    public static ExtractableResponse<Response> 제품_등록_요청(String token, ProductRequest productRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(productRequest))
                .when()
                        .post()
                .then()
                        .log().all()
                        .extract();
        return response;
    }

    public static ExtractableResponse<Response> 제품_조회_요청(Long productId) {
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/products/" + productId)
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .extract();
        return response;
    }

    public static ExtractableResponse<Response> 제품_조회_요청(Long productId, String token) {
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/products/" + productId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .extract();
        return response;
    }

    public static void 제품_예약된_수량이_변경된다(ExtractableResponse<Response> 제품_조회_결과, Integer quantity) {
        ProductDetailResponse response = 제품_조회_결과.as(ProductDetailResponse.class);
        assertThat(response.getReservedStock()).isEqualTo(quantity);
    }

    public static void 제품_구매가능_수량이_변경된다(ExtractableResponse<Response> 제품_조회_결과, Integer quantity) {
        ProductDetailResponse response = 제품_조회_결과.as(ProductDetailResponse.class);
        assertThat(response.getStock()).isEqualTo(quantity);
    }

    public static void 제품_상태가_변경된다(ExtractableResponse<Response> 제품_조회_결과, ProductStatus productStatus) {
        ProductDetailResponse response = 제품_조회_결과.as(ProductDetailResponse.class);
        assertThat(response.getStatus()).isEqualTo(productStatus);
    }

    public static void 제품에_대한_요청_목록의_사이즈가_변경된다(ExtractableResponse<Response> 제품_조회_결과, int size) {
        ProductDetailResponse response = 제품_조회_결과.as(ProductDetailResponse.class);
        assertThat(response.getOffers().size()).isEqualTo(size);
    }
}
