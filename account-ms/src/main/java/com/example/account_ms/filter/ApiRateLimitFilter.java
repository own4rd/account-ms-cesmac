package com.example.account_ms.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApiRateLimitFilter implements Filter {
    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // Trocar por env var
        int CAPACITY = 20;
        // Trocar por env var
        int MINUTES_LIMIT = 1;
        Bandwidth limit = Bandwidth.builder()
                .capacity(CAPACITY)
                .refillGreedy(CAPACITY, Duration.ofMinutes(MINUTES_LIMIT))
                .build();
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String ip = req.getRemoteAddr();
        Bucket bucket = ipBucketMap.computeIfAbsent(ip, k -> createNewBucket());
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpResp = (HttpServletResponse) response;
            int TOO_MANY_REQUESTS = 429;
            httpResp.setStatus(TOO_MANY_REQUESTS);
            httpResp.getWriter().write("Rate limit exceeded. Try again later.");
        }
    }
}
