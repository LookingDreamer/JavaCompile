package com.common.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.cninsure.core.utils.LogUtil;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null || values.length == 0) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }

        //System.out.println("getParameterValues:" + Arrays.asList(encodedValues));
        return encodedValues;
    }

    public ServletInputStream getInputStream() throws IOException {
        String value = getRequestPostStr();
        if (null == value || "".equals(value)) {
            return null;
        }

        //判断是否json数据
        if (isGoodJson(value)) {
            value = cleanXSS(value);
            LogUtil.debug("json进行特殊字符过滤:" + value);
        }
        final ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(value.getBytes("UTF-8"));

        ServletInputStream inputStream = new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return arrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };

        return inputStream;
    }

    public String getParameter(String parameter) {

        String value = super.getParameter(parameter);
        if (value == null || value.equals("")) {
            return null;
        }
        //System.out.println("getParameter:" + value);
        return cleanXSS(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        //System.out.println("getHeader:" + value);
        return cleanXSS(value);
    }

    /**
     * 判断是否json格式
     *
     * @param json
     * @return
     */
    private boolean isGoodJson(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            //LogUtil.info("不是 json数据: " + json);
            return false;
        }
    }

    /**
     * 过滤特殊字符
     *
     * @param value
     * @return
     */
    private String cleanXSS(String value) {
        if (null != value) {
            value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
            //value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
            value = value.replaceAll("'", "& #39;");
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("%", "％");//%(?![0-9a-fA-F][0-9a-fA-F])
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            value = value.replaceAll("script", "");
        }
        return value;
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     *
     * @param request
     * @return
     * @throws IOException
     */
    private byte[] getRequestPostBytes() throws IOException {
        int contentLength = super.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = super.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 描述:获取 post 请求内容
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String getRequestPostStr() throws IOException {
        byte buffer[] = getRequestPostBytes();
        String charEncoding = super.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }

}
