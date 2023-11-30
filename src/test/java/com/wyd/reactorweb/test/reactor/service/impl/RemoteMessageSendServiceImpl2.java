package com.wyd.reactorweb.test.reactor.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wyd.reactorweb.common.AjaxResult;
import com.wyd.reactorweb.common.AjaxResultUtil;
import com.wyd.reactorweb.design.reactor.core.AsynReceptResult;
import com.wyd.reactorweb.test.reactor.entity.SendInfo;
import com.wyd.reactorweb.test.reactor.service.RemoteMessageSendService;
import com.wyd.reactorweb.util.MyThreadPoolExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: TMSP
 * @description: RemoteMessageSendService的实现类，此处直接实现了，正常来说这边应该是远程调用其他系统的接口。
 * @author: Stone
 * @create: 2023-07-31 10:12
 **/
@Service
public class RemoteMessageSendServiceImpl2 implements RemoteMessageSendService {

    // 线程池执行释放 lock 任务
    private final ThreadPoolExecutor executor =  new MyThreadPoolExecutor(1, 1, 0L, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(1000), new ThreadFactoryBuilder().setNameFormat("remoteMessageSendServiceImpl2-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());

    // 确保队列线程安全
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private final Snowflake snowflake = new Snowflake();

    private final Map<String, SendInfo> paramMap = new ConcurrentHashMap<>();

    private final Queue<String> callIdQueue = new ConcurrentLinkedQueue<>();

    @Override
    public AjaxResult<String> send(SendInfo sendInfo) {
        String callId = snowflake.nextIdStr();
        AjaxResult<String> result = new AjaxResult<>();
        result.setData(callId);
        // 缓存 string 到 map 中，注意要先 put ，然后再往队列里加 callId，注意线程安全问题
        paramMap.put(callId, sendInfo);
        callIdQueue.add(callId);
        // 提交一个释放等待 callIdQueue 的任务
        executor.submit(() -> {
            if (!callIdQueue.isEmpty()) {
                lock.lock();
                try {
                    condition.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });
        return AjaxResultUtil.getTrueAjaxResult(result);
    }

    @Override
    public AsynReceptResult<Map<String, AjaxResult<Boolean>>> getResultList() {
        if (callIdQueue.isEmpty()) {
            // 没有需要处理的业务，等待，防止调用方疲劳处理
            lock.lock();
            try {
                condition.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return null;
        }

        try {
            // 模拟处理请求需要一定的时间
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<String> callIds = new ArrayList<>();
        List<SendInfo> sendInfos = new ArrayList<>();
        do {
            String callId = callIdQueue.poll();
            if (StrUtil.isNotBlank(callId)) {
                sendInfos.add(paramMap.remove(callId));
                callIds.add(callId);
            }
        } while (callIds.size() <= 1000 && !callIdQueue.isEmpty());

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
                // System.out.println("结果处理了，包含reactor信息...");
                result.setData(true);
                return AjaxResultUtil.getTrueAjaxResult(result);
            }
        }
        result.setData(false);
        return AjaxResultUtil.getTrueAjaxResult(result);
    }
}
