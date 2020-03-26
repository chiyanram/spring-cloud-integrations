package com.rmurugaian.spring.cloud;

import com.pszymczyk.consul.ConsulPorts;
import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;

import javax.annotation.PreDestroy;


public class LocalConsulServer {

    private final ConsulProcess consulProcess;

    public LocalConsulServer(final LocalConsulPorts ports) {
        consulProcess = ConsulStarterBuilder.consulStarter()
                .withConsulPorts(ConsulPorts.consulPorts()
                        .withDnsPort(ports.getDnsPort())
                        .withHttpPort(ports.getHttpPort())
                        .withSerfLanPort(ports.getSerfLanPort())
                        .withSerfWanPort(ports.getSerfWanPort())
                        .withServerPort(ports.getServerPort())
                        .build())
                .build()
                .start();
    }

    @PreDestroy
    public void stopConsul() {
        consulProcess.close();
    }

    ConsulProcess getConsulProcess() {
        return consulProcess;
    }
}
