package com.blogapp.backend.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(JwtAuthenticationFilter.class);
    @Autowired private JwtTokenHelper jwtTokenHelper;

    @Autowired private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        //1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String requestToken = httpServletRequest.getHeader("Authorization");

        //2. validate the header and check the prefix
        String userName = null;
        String jwtToken = null;

        if(requestToken!=null&&requestToken.startsWith("Bearer")){
            jwtToken = requestToken.substring(7);

            try {
                userName = this.jwtTokenHelper.extractUsername(jwtToken);
            }catch (IllegalArgumentException e){
                LOGGER.error("Unable to get JWT Token.");
            }
            catch (ExpiredJwtException e){
                LOGGER.error("JWT Token Expired.");
            }
            catch (MalformedJwtException e){
                LOGGER.error("Invalid JWT Token");
            }
        }else {
            LOGGER.error("Invalid Token Formation .[Token Dose Not Starts with : Bearer] : {}",requestToken);
        }
        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

            if(Boolean.TRUE.equals(this.jwtTokenHelper.validateToken(jwtToken,userDetails))){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(jwtToken,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else{
                LOGGER.error("Invalid Token");
            }
        }else{
            LOGGER.error("Invalid User Name, {}",userName);
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
