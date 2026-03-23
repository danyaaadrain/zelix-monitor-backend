package ru.outofmemory.zelixmonitorbackend.dto.cgi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetMinerConfResponse {

    public List<Pool> pools;

    @JsonProperty("bitmain-work-mode")
    public String work_mode;

    public static class Pool {
        public String url;
        public String user;
        public String pass;
    }


}
