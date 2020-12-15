package com.mr.filter;

import com.mr.config.FilterProperties;
import com.mr.config.JwtConfig;
import com.mr.util.JwtUtils;
import com.mr.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private FilterProperties filterProperties;

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();
        return  this.isAllowPath(requestURI);
    }

    /**
     * 判断url 是否是白名单中的 如果是就返回false 不是就返回true
     * @param url
     * @return
     */
    public  Boolean isAllowPath(String url){
       for (String  path : filterProperties.getAllowPaths()){
           if(url.startsWith(path)){
               return  false;
           }
       }
        return  true;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        System.out.println("拦截到请求"+request.getRequestURI());
        // 获取token
        String token = CookieUtils.getCookieValue(request, jwtConfig.getCookieName());
        System.out.println("token信息"+token);
         // 校验
        try {
            // 通过公钥解密，如果成功，就放行，失败就拦截
            JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
        } catch (Exception e) {
            System.out.println("解析失败 拦截"+token);
            // 校验出现异常，返回403
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
        }
        return null;
    }
}