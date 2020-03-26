package com.rmurugaian.spring.cloud;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties("consul.local")
public class LocalConsulConfiguration {

    @NestedConfigurationProperty
    private LocalConsulPorts ports = new LocalConsulPorts();

    @Bean
    public LocalConsulServer localConsulServer() {
        return new LocalConsulServer(ports);
    }

    public LocalConsulPorts getPorts() {
        return ports;
    }

    public void setPorts(final LocalConsulPorts ports) {
        this.ports = ports;
    }
}
