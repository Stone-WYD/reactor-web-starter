package com.wyd.reactorweb.test.reactor.demo;


import com.wyd.reactorweb.common.AjaxResult;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class SendInfo {

    private static final long serialVersionUID = -532388861282783204L;

    // 操作人姓名
    private String userName;

    // 短信标题
    private String title;

    // 短信类型
    private String type;

    // 接口平台调用的参数
    private String data;

    // 发送的短信内容
    private String content;

    // 手机号，单发接口不能为空
    private String mobile;

    // 群发手机号，群发接口不能为空
    private String mobiles;

    private List<String> mobileList;

    // 短信发送类型 1--云片平台发送，2--创蓝平台
    private String sendType;

    // 法院代码
    private String courtCode;

    // 发送方式，1.单发，2.群发
    private String sendWay;

    private Long singleId;

    private Long groupId;

    private Map<String, AjaxResult> resultMap;

    private boolean validatePass;

}
