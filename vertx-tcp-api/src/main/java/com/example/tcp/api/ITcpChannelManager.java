package com.example.tcp.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ITcpChannelManager {

    void create(int port);

}
