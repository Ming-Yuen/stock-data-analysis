package com.stockinsight.config;

import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class IbkrApiConfig {

    @Bean(destroyMethod = "eDisconnect")
    public EClientSocket ibClient(IbWrapper wrapper) {
        EJavaSignal signal = new EJavaSignal();
        EClientSocket client = new EClientSocket(wrapper, signal);
        // 连接到本机 TWS Paper Trading，端口 7497，clientId 自行设置
        client.eConnect("127.0.0.1", 7497, 0);
        // 启动后台线程处理消息
        final EReader reader = new EReader(client, signal);
        reader.start();
        new Thread(() -> {
            while (client.isConnected()) {
                signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return client;
    }
}

