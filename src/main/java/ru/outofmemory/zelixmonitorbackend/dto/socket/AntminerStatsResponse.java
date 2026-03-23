package ru.outofmemory.zelixmonitorbackend.dto.socket;

import java.util.List;

public class AntminerStatsResponse {

    public List<Stats> STATS;

    public static class Stats {
        public long elapsed;

        public double rate_5s;
        public double rate_avg;
        public double rate_30m;
        public double rate_ideal;
        public String rate_unit;

        public int chain_num;
        public int fan_num;
        public int power;
        public int temp_ctrlboard;

        public int[] fan;
        public double hwp_total;

        public List<Chain> chain;
    }

    public static class Chain {
        public int index;
        public int freq_avg;

        public double rate_ideal;
        public double rate_real;

        public int asic_num;
        public String asic;

        public int[] temp_pic;
        public int[] temp_pcb;
        public int[] temp_chip;

        public long hw;
        public boolean eeprom_loaded;
        public String sn;
    }
}
