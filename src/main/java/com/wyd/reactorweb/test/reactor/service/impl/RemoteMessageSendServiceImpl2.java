package com.wyd.reactorweb.test.reactor.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.wyd.reactorweb.common.AjaxResult;
import com.wyd.reactorweb.common.AjaxResultUtil;
import com.wyd.reactorweb.design.reactor.core.AsynReceptResult;
import com.wyd.reactorweb.test.reactor.entity.SendInfo;
import com.wyd.reactorweb.test.reactor.service.RemoteMessageSendService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @program: TMSP
 * @description: RemoteMessageSendService的实现类，此处直接实现了，正常来说这边应该是远程调用其他系统的接口。
 * @author: Stone
 * @create: 2023-07-31 10:12
 **/
@Service
public class RemoteMessageSendServiceImpl2 implements RemoteMessageSendService {

    private final Snowflake snowflake = new Snowflake();

    private final Map<String, SendInfo> paramMap = new ConcurrentHashMap<>();

    private final Queue<String> callIdQueue = new ConcurrentLinkedQueue<>();

    @Override
    public AjaxResult<String> send(SendInfo sendInfo) {
        String callId = snowflake.nextIdStr();
        AjaxResult<String> result = new AjaxResult<>();
        result.setData(callId);
        // 缓存 string 到 map 中
        callIdQueue.add(callId);
        paramMap.put(callId, sendInfo);
        return AjaxResultUtil.getTrueAjaxResult(result);
    }

    @Override
    public AsynReceptResult<Map<String, AjaxResult<Boolean>>> getResultList() {
        if (callIdQueue.isEmpty()) {
            // 没有需要处理的业务
            return null;
        }
        List<String> callIds = new ArrayList<>();
        do {
            String callId = callIdQueue.poll();
            callIds.add(callId);
        } while (callIds.size() <= 10 && !callIdQueue.isEmpty());
        List<SendInfo> sendInfos = new ArrayList<>();
        callIds.forEach(callId -> sendInfos.add(paramMap.remove(callId)));

        // 获取结果
        Map<String, AjaxResult<Boolean>> resultData = new HashMap<>();

        for (int i = 0; i < callIds.size(); i++) {
            String callId = callIds.get(i);
            SendInfo sendInfo = sendInfos.get(i);
            AjaxResult<Boolean> result = handleBusiness(sendInfo);
            resultData.put(callId, result);
        }

        // 返回结果
        return new AsynReceptResult(null, true, resultData);
    }

    private AjaxResult<Boolean> handleBusiness(SendInfo sendInfo) {
        String content = sendInfo.getContent();
        AjaxResult<Boolean> result = new AjaxResult<>();
        // 根据内容判断结果
        if (StrUtil.isNotBlank(content)) {
            if (content.contains("reactor")) {
                System.out.println("结果处理了，包含reactor信息...");
                result.setData(true);
                return AjaxResultUtil.getTrueAjaxResult(result);
            }
        }
        result.setData(false);
        return AjaxResultUtil.getTrueAjaxResult(result);
    }
}
