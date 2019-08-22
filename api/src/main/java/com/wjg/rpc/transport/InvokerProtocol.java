package com.wjg.rpc.transport;

import java.io.Serializable;

public class InvokerProtocol implements Serializable {

    private static final long serialVersionUID = -4539929645305581779L;

    private String className;
    private String methodNme;
    private Class<?>[] paramtypes;
    private Object[] params;

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodNme(String methodNme) {
        this.methodNme = methodNme;
    }

    public void setParamtypes(Class<?>[] paramtypes) {
        this.paramtypes = paramtypes;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodNme() {
        return methodNme;
    }

    public Class<?>[] getParamtypes() {
        return paramtypes;
    }

    public Object[] getParams() {
        return params;
    }

    public InvokerProtocol className(String className){
        this.setClassName(className);
        return this;
    }
    public InvokerProtocol methodNme(String methodNme){
      this.setMethodNme(methodNme);return this;
    }
    public InvokerProtocol paramtypes(Class<?>[] paramtypes){
      this.setParamtypes(paramtypes);return this;
    }
    public InvokerProtocol params(Object[] params){
      this.setParams(params);return this;
    }

}
