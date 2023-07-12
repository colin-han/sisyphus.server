package info.colinhan.sisyphus.server.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Controller
public class ForwardingController {
    private final RestTemplate restTemplate;

    @Autowired
    public ForwardingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/**")
    public void forwardRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Forwarding request: {}", request.getRequestURI());
        String requestedPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String queryString = request.getQueryString();
        String targetUrl = "http://localhost:3001/" + requestedPath + (queryString != null ? "?" + queryString : "");

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName.equalsIgnoreCase("accept-encoding")) {
                continue;
            }
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                headers.add(headerName, headerValue);
            }
        }

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        HttpHeaders responseHeaders = responseEntity.getHeaders();
        for (String headerName : responseHeaders.keySet()) {
            for (String headerValue : responseHeaders.get(headerName)) {
                response.addHeader(headerName, headerValue);
            }
        }

        response.getWriter().write(responseEntity.getBody());
        response.getWriter().flush();
        response.setStatus(responseEntity.getStatusCodeValue());
    }
}