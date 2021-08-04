package com.bookcity.filter;

import com.alibaba.fastjson.JSON;
import com.bookcity.service.ResponseObj;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String reqURI = req.getRequestURI();
        String uriSuffix = reqURI.substring(reqURI.lastIndexOf('/'));

        String token = req.getHeader("token");
        System.out.println(token);
        if(token == null && !(uriSuffix.equals("/login") || uriSuffix.equals("/register"))){
            response.getWriter().write(JSON.toJSONString(new ResponseObj(4, "需要token",null)));
            return;
        }
        chain.doFilter(request, response);
    }
}
