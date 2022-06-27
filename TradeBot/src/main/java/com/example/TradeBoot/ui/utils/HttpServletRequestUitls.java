package com.example.TradeBoot.ui.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpServletRequestUitls {

    public static String getPreventPath(HttpServletRequest request) throws MalformedURLException {
        var url = new URL(request.getHeader("referer"));
        return url.getPath();
    }
}
