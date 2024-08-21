package com.wynd.vop.framework.config;

import com.amazonaws.regions.Regions;

import java.net.URI;

public abstract class BipAwsServiceEndpoint {

    private String region;
    private String endpoint;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBaseUrl() {
        URI endpointUri = URI.create(endpoint);

        StringBuilder baseUrlBuilder = new StringBuilder();
        baseUrlBuilder.append("http://");
        baseUrlBuilder.append(endpointUri.getHost());

        if(endpointUri.getPort() > 0) {
            baseUrlBuilder.append(":");
            baseUrlBuilder.append(endpointUri.getPort());
        }

        return baseUrlBuilder.toString();
    }

    public Regions getRegions() {
        return Regions.fromName(region);
    }
}
