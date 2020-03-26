package com.rmurugaian.spring.cloud;


public class LocalConsulPorts {

    private int dnsPort = 8600;
    private int httpPort = 8500;
    private int serfLanPort = 8301;
    private int serfWanPort = 8302;
    private int serverPort = 8300;

    public int getDnsPort() {
        return dnsPort;
    }

    public void setDnsPort(final int dnsPort) {
        this.dnsPort = dnsPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(final int httpPort) {
        this.httpPort = httpPort;
    }

    public int getSerfLanPort() {
        return serfLanPort;
    }

    public void setSerfLanPort(final int serfLanPort) {
        this.serfLanPort = serfLanPort;
    }

    public int getSerfWanPort() {
        return serfWanPort;
    }

    public void setSerfWanPort(final int serfWanPort) {
        this.serfWanPort = serfWanPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(final int serverPort) {
        this.serverPort = serverPort;
    }
}
