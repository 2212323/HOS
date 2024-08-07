package com.example.demo4444.demos.web.demo;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class IpFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String remoteAddr = request.getRemoteAddr();

        // IP白名单
        //if(false){
        if (true||"192.168.1.100".equals(remoteAddr) || "192.168.1.101".equals(remoteAddr)) {
            filterChain.doFilter(request, response); // 允许通过
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 拒绝其他IP
        }
    }
}
