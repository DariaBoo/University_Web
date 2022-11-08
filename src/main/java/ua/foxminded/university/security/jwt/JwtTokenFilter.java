package ua.foxminded.university.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.security.jwt.exception.InvalidTokenException;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String username = null;
        String jwtToken = null;
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("[ON doFilterInternal]:: header {}", header);
        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);
            log.info("[ON doFilterInternal]:: jwtToken [ {} ]", jwtToken);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                log.info("[ON doFilterInternal]:: username [ {} ]", username);
            } catch (InvalidTokenException e) {
                request.setAttribute("exception", e.getLocalizedMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/expired-jwt");
                dispatcher.forward(request, response);
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("[ON doFilterInternal]:: starting token validation...");
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("[ON doFilterInternal]:: set authentication to SecurityContextHolder - {}", authToken);
            }
        }
        log.info("[ON doFilterInternal]:: filtering request and response by FilterChain");
        filterChain.doFilter(request, response);
    }
}
