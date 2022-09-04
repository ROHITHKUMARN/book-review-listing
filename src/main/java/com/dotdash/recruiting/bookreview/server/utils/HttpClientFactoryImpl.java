package com.dotdash.recruiting.bookreview.server.utils;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class HttpClientFactoryImpl implements HttpClientFactory {
    @Override
    public CloseableHttpClient createCloseableHttpClient() {
        return HttpClientBuilder.create().build();
    }
}
