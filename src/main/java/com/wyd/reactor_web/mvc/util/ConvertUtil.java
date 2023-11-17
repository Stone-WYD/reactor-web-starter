package com.wyd.reactor_web.mvc.util;

import com.wyd.reactor_web.mvc.invoke.request.MyNativeWebRequest;
import com.wyd.reactor_web.mvc.invoke.request.MyHttpServletRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @program: reactor_web
 * @description: 转换工具类
 * @author: Stone
 * @create: 2023-11-17 15:14
 **/
public class ConvertUtil {

    public static Object convertStringToPrimitive(Class<?> primitiveType, String value) {
        if (primitiveType == int.class || primitiveType == Integer.class) {
            return Integer.parseInt(value);
        } else if (primitiveType == long.class || primitiveType == Long.class) {
            return Long.parseLong(value);
        } else if (primitiveType == double.class || primitiveType == Double.class) {
            return Double.parseDouble(value);
        } else if (primitiveType == float.class || primitiveType == Float.class) {
            return Float.parseFloat(value);
        } else if (primitiveType == short.class || primitiveType == Short.class) {
            return Short.parseShort(value);
        } else if (primitiveType == byte.class || primitiveType == Byte.class) {
            return Byte.parseByte(value);
        } else if (primitiveType == char.class || primitiveType == Character.class) {
            return value.charAt(0);
        } else if (primitiveType == boolean.class || primitiveType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else {
            return null;
        }
    }

    public static NativeWebRequest convertFullHttpRequestToNativeWebRequest(FullHttpRequest httpRequest) throws UnsupportedEncodingException {
        return new InnerNativeWebRequest(httpRequest);
    }

    public static ServletRequest convertFullHttpRequestToServletRequest(FullHttpRequest httpRequest) throws UnsupportedEncodingException {
        return new InnerHttpServletRequest(httpRequest);
    }

    public static class InnerNativeWebRequest extends MyNativeWebRequest {
        private final Map<String, String[]> queryParam = new LinkedHashMap<>(16);
        private final HttpHeaders headers;

        public InnerNativeWebRequest(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
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
        public String[] getHeaderValues(String name) {
            List<String> all = headers.getAll(name);
            return all.toArray(new String[0]);
        }

        @Override
        public Iterator<String> getHeaderNames() {
            Set<String> all = headers.names();
            return all.iterator();
        }

        @Override
        public String getHeader(String name) {
            return headers.get(name);
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
        public Iterator<String> getParameterNames() {
            return queryParam.keySet().iterator();
        }

        @Override
        public String[] getParameterValues(String name) {
            return queryParam.get(name);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return queryParam;
        }
    }


    public static class InnerHttpServletRequest extends MyHttpServletRequest {
        private final Map<String, String[]> queryParam = new LinkedHashMap<>(16);
        private final HttpHeaders headers;

        public InnerHttpServletRequest(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
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

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
            return null;
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

}
