package ru.outofmemory.zelixmonitorbackend.dto.socket;

import java.util.List;

public class AntminerPoolsResponse {

    public List<Pools> POOLS;

    public static class Pools {
        public int index;
        public String url;
        public String user;
        public String status;
        public int priority;

        public long getworks;
        public long accepted;
        public long rejected;
        public long discarded;
        public long stale;

        public String diff;
        public long diff1;
        public long diffa;
        public long diffr;
        public long diffs;

        public long lsdiff;
        public String lstime;
    }
}