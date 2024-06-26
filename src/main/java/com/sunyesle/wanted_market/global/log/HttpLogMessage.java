package com.sunyesle.wanted_market.global.log;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.rightPad;

public class HttpLogMessage {
    private static final String[] IP_HEADER_NAMES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "X-Real-IP",
            "X-RealIP",
            "REMOTE_ADDR"
    };

    private final String method;
    private final String requestURI;
    private final HttpStatus httpStatus;
    private final String clientIp;
    private final double elapsedTime;
    private final Map<String, String> headers;
    private final Map<String, String> requestParam;
    private final String requestBody;
    private final String responseBody;

    public HttpLogMessage(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, double elapsedTime) {
        this.method = requestWrapper.getMethod();
        this.requestURI = requestWrapper.getRequestURI();
        this.httpStatus = HttpStatus.valueOf(responseWrapper.getStatus());
        this.clientIp = getClientIp(requestWrapper);
        this.elapsedTime = elapsedTime;
        this.headers = getHeaders(requestWrapper);
        this.requestParam = getParams(requestWrapper);
        this.requestBody = new String(requestWrapper.getContentAsByteArray());
        this.responseBody = new String(responseWrapper.getContentAsByteArray());
    }

    private String getClientIp(HttpServletRequest request) {
        for (String header : IP_HEADER_NAMES) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> jsonObject = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String replaceHeader = headerName.replace("\\.", "-");
            jsonObject.put(replaceHeader, request.getHeader(headerName));
        }
        return jsonObject;
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> jsonObject = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String replaceParam = paramName.replace("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(paramName));
        }
        return jsonObject;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(rightPad("Request method:", 16)).append(this.method).append("\n");
        sb.append(rightPad("Request URI:", 16)).append(this.requestURI).append("\n");
        sb.append(rightPad("Request params:", 16)).append(this.requestParam).append("\n");
        sb.append(rightPad("Headers:", 16)).append(this.headers).append("\n");
        sb.append(rightPad("Request Body:", 16)).append(this.requestBody).append("\n");
        sb.append(rightPad("Status:", 16)).append(this.httpStatus).append("\n");
        sb.append(rightPad("Response Body:", 16)).append(responseBody).append("\n");
        sb.append(rightPad("Client IP:", 16)).append(this.clientIp).append("\n");
        sb.append(rightPad("Elapsed Time:", 16)).append(this.elapsedTime).append("\n");

        return sb.toString();
    }
}
