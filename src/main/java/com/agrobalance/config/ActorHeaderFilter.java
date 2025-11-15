package com.agrobalance.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ActorHeaderFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    String id = req.getHeader("X-Actor-Id");
    String name = req.getHeader("X-Actor-Name");
    String actor = (id != null && name != null) ? id + ":" + name : (name != null ? name : (id != null ? id : "anonymous"));
    try {
      AuditorContext.set(actor);
      chain.doFilter(req, res);
    } finally {
      AuditorContext.clear();
    }
  }
}
