package ru.outofmemory.zelixmonitorbackend.dto.cgi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetSystemInfoResponse {
    @JsonProperty("minertype")
    public String minertype;
    @JsonProperty("macaddr")
    public String mac;
    @JsonProperty("ipaddress")
    public String ip;
    @JsonProperty("Algorithm")
    public String algo;
    @JsonProperty("serinum")
    public String sn;
}
