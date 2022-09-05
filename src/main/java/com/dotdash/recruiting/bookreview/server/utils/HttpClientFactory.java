package com.dotdash.recruiting.bookreview.server.utils;

import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpClientFactory {
    public CloseableHttpClient createCloseableHttpClient();
}