package com.sunyesle.wanted_market;

import com.sunyesle.wanted_market.member.dto.SigninRequest;
import com.sunyesle.wanted_market.member.dto.SigninResponse;
import com.sunyesle.wanted_market.member.dto.SignupRequest;
import com.sunyesle.wanted_market.member.dto.SignupResponse;
import com.sunyesle.wanted_market.member.MemberRepository;
import com.sunyesle.wanted_market.support.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sunyesle.wanted_market.support.AuthSteps.로그인_요청;
import static com.sunyesle.wanted_market.support.AuthSteps.회원가입_요청;
import static com.sunyesle.wanted_market.support.CommonSupporter.등록에_성공한다;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입을_한다() {
        String name = "김이름";
        String email = "test@email.com";
        String password = "password";
        SignupRequest signupRequest = new SignupRequest(name, email, password);

        ExtractableResponse<Response> response = 회원가입_요청(signupRequest);

        등록에_성공한다(response);
        SignupResponse signupResponse =  response.as(SignupResponse.class);
        assertThat(signupResponse.getId()).isNotNull();
    }

    @Test
    void 로그인을_한다() {
        String name = "김이름";
        String email = "test@email.com";
        String password = "password";
        SignupRequest signupRequest = new SignupRequest(name, email, password);
        회원가입_요청(signupRequest);
        SigninRequest signinRequest = new SigninRequest(email, password);

        ExtractableResponse<Response> response = 로그인_요청(signinRequest);

        SigninResponse signinResponse = response.as(SigninResponse.class);
        assertThat(signinResponse.getToken()).isNotBlank();
    }
}
