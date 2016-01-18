package com.pixshow.framework.exception.api;

public class LogicException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 6772006553291150019L;

    private int               code             = 0;

    public LogicException(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public LogicException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
