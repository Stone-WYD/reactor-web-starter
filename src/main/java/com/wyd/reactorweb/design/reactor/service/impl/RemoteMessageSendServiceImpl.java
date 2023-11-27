package com.wyd.reactorweb.design.reactor.service.impl;

import com.wyd.reactorweb.common.AjaxResult;
import com.wyd.reactorweb.design.reactor.core.AsynReceptResult;
import com.wyd.reactorweb.design.reactor.demo.SendInfo;
import com.wyd.reactorweb.design.reactor.service.RemoteMessageSendService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: TMSP
 * @description: RemoteMessageSendService的实现类
 * @author: Stone
 * @create: 2023-07-31 10:12
 **/
@Service
public class RemoteMessageSendServiceImpl implements RemoteMessageSendService {

    @Override
    public AjaxResult<String> send(SendInfo sendInfo) {
        return null;
    }

    @Override
    public AsynReceptResult<Map<String, AjaxResult<Boolean>>> getResultList() {
        return null;
    }
}
