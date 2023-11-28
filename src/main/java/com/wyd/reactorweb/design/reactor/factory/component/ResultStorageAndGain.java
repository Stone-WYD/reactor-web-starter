package com.wyd.reactorweb.design.reactor.factory.component;

import com.wyd.reactorweb.design.reactor.core.ChannelContext;

public interface ResultStorageAndGain {

    ChannelContext getResult(String callId);

    void invalidResult(String callId);

    void storageResult(String callId, ChannelContext data);
}
