package com.mikkaeru.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebUtils {

    private final HttpServletRequest request;

    public WebUtils(HttpServletRequest request) {
        this.request = request;
    }

    public String getUserAgent() {
        return getHeader("user-agent");
    }

    public String getClientIp() {
        return getHeader("X-FORWARDED-FOR");
    }

    private String getHeader(String name) {
        String tmp = "";

        if (request != null) {
            tmp = request.getHeader(name);

            if (tmp == null || "".equals(tmp)) {
                tmp = request.getRemoteAddr();
            }
        }

        return tmp;
    }
}
