package ru.outofmemory.zelixmonitorbackend.dto.socket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AntminerVersionResponse {
    @JsonProperty("STATUS")
    public List<StatusInfo> status;

    @JsonProperty("VERSION")
    public List<VersionInfo> version;

    @JsonProperty("id")
    public int id;

    public String getType() {
        return this.version.getFirst().type;
    }

    public boolean hasErrors() {
        return !this.status.getFirst().status.equals("S");
    }

    public static class VersionInfo {
        @JsonProperty("API")
        public String api;
        @JsonProperty("BMMiner")
        public String bminer;
        @JsonProperty("CompileTime")
        public String compileTime;
        @JsonProperty("Miner")
        public String miner;
        @JsonProperty("Type")
        public String type;
    }

    public static class StatusInfo {
        @JsonProperty("STATUS")
        public String status;
    }
}
