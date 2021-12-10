package com.oven.demo.framework.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        } else {
            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; ++i) {
                encodedValues[i] = this.cleanXss(values[i]);
            }
            return encodedValues;
        }
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return value == null ? null : this.cleanXss(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        return value == null ? null : this.cleanXss(value);
    }

    private String cleanXss(String value) {
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\"'][\\s]*javascript:(.*)[\"']", "\"\"");
        return value;
    }

}
