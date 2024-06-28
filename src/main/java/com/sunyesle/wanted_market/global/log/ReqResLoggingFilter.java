package com.sunyesle.wanted_market.global.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class ReqResLoggingFilter extends OncePerRequestFilter {
    private static final String REQUEST_ID = "request_id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(REQUEST_ID, requestId);

        long startTimeMillis = System.currentTimeMillis();
        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);
        long endTimeMillis = System.currentTimeMillis();

        double elapsedTime = (endTimeMillis - startTimeMillis) / 1000.0;

        HttpLogMessage httpLogMessage = new HttpLogMessage(cachingRequestWrapper, cachingResponseWrapper, elapsedTime);
        cachingResponseWrapper.copyBodyToResponse();

        log.info(httpLogMessage.print());
        MDC.remove(REQUEST_ID);
    }
}
