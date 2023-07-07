package com.like4u.papermanager.common;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/5 9:51
 */
public class AjaxResult extends HashMap<String,Object> {

    public static final String CODE_TAG="code";
    public static final String MSG_TAG="msg";
    public static final String DATA_TAG="data";

    public AjaxResult() {
    }

    public AjaxResult(int code,String msg){
        super.put(CODE_TAG,code);
        super.put(MSG_TAG,msg);
    }

    /**
     * 初始化消息
     * @param code 状态码
     * @param msg 消息
     * @param data 数据
     */
    public AjaxResult(int code,String msg,Object data){
        super.put(CODE_TAG,code);
        super.put(MSG_TAG,msg);
        if (data!=null)
        {
            super.put(DATA_TAG, data);
        }
    }

    /***
     *只返回成功消息
     * @return 成功消息
     */
    public static AjaxResult success(){
        return  success("成功！");
    }

    /**
     *
     * @param data 返回数据
     * @return 成功消息
     */
    public static AjaxResult success(Object data){
        return success("操作成功",data);
    }

    /***
     *
     * @param msg 返回的消息内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg){
        return success(msg,null);
    }

    /***
     *
     * @param msg 返回消息
     * @param data 返回数据
     * @return 成功消息
     */
    public static AjaxResult success(String msg,Object data){
        return success(msg,HttpStatus.SUCCESS,data);
    }

    public static AjaxResult success(String msg,int code,Object data){
        return new AjaxResult(code,msg,data);
    }

    public static AjaxResult warn(){
        return warn("操作错误");
    }
    public static AjaxResult warn(String msg){
        return warn(msg,null);
    }

    public static AjaxResult warn(String msg,Object data){
        return new AjaxResult(HttpStatus.WARN,msg,data);
    }
    public static AjaxResult error()
    {
        return AjaxResult.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 错误消息
     */
    public static AjaxResult error(String msg)
    {
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static AjaxResult error(String msg, Object data)
    {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息,自定义错误状态码
     *
     * @param code 状态码
     * @param msg 返回内容
     * @return 错误消息
     */
    public static AjaxResult error(int code, String msg)
    {
        return new AjaxResult(code, msg, null);
    }

    /**
     * 是否为成功消息
     *
     * @return 结果
     */
    public boolean isSuccess()
    {
        return Objects.equals(HttpStatus.SUCCESS, this.get(CODE_TAG));
    }

    /**
     * 是否为警告消息
     *
     * @return 结果
     */
    public boolean isWarn()
    {
        return Objects.equals(HttpStatus.WARN, this.get(CODE_TAG));
    }

    /**
     * 是否为错误消息
     *
     * @return 结果
     */
    public boolean isError()
    {
        return Objects.equals(HttpStatus.ERROR, this.get(CODE_TAG));
    }


}
