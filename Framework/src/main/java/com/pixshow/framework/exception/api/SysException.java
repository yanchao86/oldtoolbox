package com.pixshow.framework.exception.api;

import java.util.Map;

public class SysException extends RuntimeException {
    private static final long   serialVersionUID = -5309574770175613338L;

    private String              code             = null;
    private Map<String, Object> info             = null;

    public SysException() {
        super();
    }

    public SysException(String message) {
        super(message);
    }

    public SysException(Throwable cause) {
        super(cause);
    }

    public SysException(String message, Throwable cause) {
        super(message, cause);
    }

    public SysException(String code, String message, Map<String, Object> info) {
        super(message);
        this.code = code;
        this.info = info;
    }

    public SysException(String code, String message, Map<String, Object> info, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

}
