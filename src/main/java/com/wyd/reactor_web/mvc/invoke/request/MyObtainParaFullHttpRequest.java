package com.wyd.reactor_web.mvc.invoke.request;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @program: reactor_web
 * @description: 为了获取参数对象而使用的 request
 * @author: Stone
 * @create: 2023-11-15 16:57
 **/
public class MyObtainParaFullHttpRequest extends MyHttpRequest{


    private final Map<String, String[]> queryParam = new LinkedHashMap<>(16);
    private final HttpHeaders headers;

    public MyObtainParaFullHttpRequest(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
        this.headers = fullHttpRequest.headers();
        String uri = fullHttpRequest.uri();
        // 填充 queryParam
        if (uri.contains("?")) {
            String paramsString = uri.substring(uri.indexOf('?'));
            String[] params = paramsString.split("&");
            for (String param : params) {
                int idx = param.indexOf('=');
                String key = URLDecoder.decode(param.substring(0, idx), "UTF-8");
                String value = URLDecoder.decode(param.substring(idx + 1), "UTF-8");
                // param 数组处理
                String[] oldArr = queryParam.get(key);
                if (oldArr != null) {
                    String[] newArr = new String[oldArr.length + 1];
                    System.arraycopy(oldArr, 0, newArr, 0, oldArr.length);
                    newArr[oldArr.length] = value;
                    queryParam.put(key, newArr);
                } else queryParam.put(key, new String[]{value});
            }
        }
    }

    // 请求头相关
    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> all = headers.getAll(name);
        return Collections.enumeration(all != null ? all : new LinkedList<>());
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> all = headers.names();
        return Collections.enumeration(all != null ? all : new LinkedList<>());
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public int getIntHeader(String name) {
        return headers.getInt(name);
    }

    @Override
    public long getDateHeader(String name) {
        return headers.getTimeMillis(name);
    }

    // 根据参数名获取参数值。
    @Override
    public String getParameter(String name) {
        if (queryParam.get(name) != null) {
           return queryParam.get(name)[0];
        }
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(queryParam.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return queryParam.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return queryParam;
    }

    /*// 上传信息相关，暂不考虑
    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return super.getPart(name);
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return super.getParts();
    }*/


    /*getParameter(String name){}; //:
    getParameterValues(String name): 根据参数名获取所有参数值。
    getParameterMap(): 获取所有参数名和参数值的映射。
    */


}
