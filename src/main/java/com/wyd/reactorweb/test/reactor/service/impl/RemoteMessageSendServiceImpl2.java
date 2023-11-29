package com.wyd.reactorweb.test.reactor.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.wyd.reactorweb.common.AjaxResult;
import com.wyd.reactorweb.common.AjaxResultUtil;
import com.wyd.reactorweb.design.reactor.core.AsynReceptResult;
import com.wyd.reactorweb.test.reactor.entity.SendInfo;
import com.wyd.reactorweb.test.reactor.service.RemoteMessageSendService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    private final Map<String, Boolean> resultMap = new ConcurrentHashMap<>();

    private final Map<String, SendInfo> paramMap = new ConcurrentHashMap<>();

    private final Queue<String> callIdQueue = new ConcurrentLinkedQueue<>();

    @PostConstruct
    public void handleBusiness() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("休眠时出现了异常，不过不影响，继续执行...");
                }

                // 每隔 100 ms 处理一次业务
                if (callIdQueue.isEmpty()) {
                    // 没有需要处理的业务
                    continue;
                }
                String callId = callIdQueue.poll();
                if (StrUtil.isNotBlank(callId)) {
                    SendInfo sendInfo = paramMap.remove(callId);
                    String content = sendInfo.getContent();
                    // 根据内容判断结果
                    if (StrUtil.isNotBlank(content)) {
                        if (content.contains("reactor")) {
                            System.out.println("结果处理了，包含reactor信息...");
                            resultMap.put(callId, true);
                            continue;
                        }
                    }
                }
                resultMap.put(callId, false);
            }
        }).start();
    }

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
        Map<String, AjaxResult<Boolean>> resultData = new HashMap<>();
        Set<Map.Entry<String, Boolean>> results;

        Iterator<Map.Entry<String, Boolean>> iterator = resultMap.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, Boolean> result = iterator.next();
            String callId = result.getKey();
            Boolean value = result.getValue();
            AjaxResult<Boolean> oneResult = new AjaxResult<>();
            oneResult.setData(value);
            resultData.put(callId, oneResult);
            // 删除这个元素
            iterator.remove();
            i++;
            if (i > 10) {
                // 一次最多操作十个元素
                break;
            }
        }

        // 返回结果
        return new AsynReceptResult(null, true, resultData);
    }
}
