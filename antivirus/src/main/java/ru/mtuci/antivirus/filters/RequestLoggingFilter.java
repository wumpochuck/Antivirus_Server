// // That filter was needed for testing client & service connection to server
//package ru.mtuci.antivirus.filters;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletInputStream;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpServletRequestWrapper;
//import org.springframework.web.filter.OncePerRequestFilter;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//
//public class RequestLoggingFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        if ("/auth/register".equals(request.getRequestURI())) {
//            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {
//                @Override
//                public ServletInputStream getInputStream() throws IOException {
//                    return request.getInputStream();
//                }
//            };
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            InputStream inputStream = requestWrapper.getInputStream();
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//            }
//            String body = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
//            System.out.println("Request body: " + body);
//        }
//        filterChain.doFilter(request, response);
//    }
//}