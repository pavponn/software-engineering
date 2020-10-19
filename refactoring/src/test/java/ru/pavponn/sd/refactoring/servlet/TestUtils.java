package ru.pavponn.sd.refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    public static HttpServletResponse mockResponse(PrintWriter writer) throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    public static HttpServletRequest mockAddRequest(String name, String price) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("price")).thenReturn(price);
        return request;
    }

    public static HttpServletRequest mockGetProductRequest() {
        return mock(HttpServletRequest.class);
    }

    public static HttpServletRequest mockQueryRequest(String cmd) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn(cmd);
        return request;
    }

}
