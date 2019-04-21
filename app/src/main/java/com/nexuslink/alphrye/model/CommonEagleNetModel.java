package com.nexuslink.alphrye.model;

/**
 * 通用猎鹰返回数据模型
 * @author yuanrui
 * @date 2019/4/21
 */
public class CommonEagleNetModel<T> {
    public String errcode;
    public String errmsg;
    public String errdetail;
    public T data;
}
