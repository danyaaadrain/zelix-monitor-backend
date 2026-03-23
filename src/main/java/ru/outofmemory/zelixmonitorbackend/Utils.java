package ru.outofmemory.zelixmonitorbackend;

import ru.outofmemory.zelixmonitorbackend.dto.ChainTempDto;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern CHAIN_PATTERN = Pattern.compile("^chain_fd\\s+(\\d+):");

    public static List<ChainTempDto> parseMinerTemp(String raw) {
        List<ChainTempDto> result = new ArrayList<>();

        Integer currentChain = null;
        List<Integer> temps = new ArrayList<>();

        for (String line : raw.split("\\R")) {
            line = line.trim();
            if (line.isEmpty()) continue;

            Matcher m = CHAIN_PATTERN.matcher(line);
            if (m.find()) {
                if (currentChain != null) {
                    result.add(build(currentChain, temps));
                }
                currentChain = Integer.parseInt(m.group(1));
                temps = new ArrayList<>();
                continue;
            }

            if (Character.isDigit(line.charAt(0))) {
                String[] parts = line.split("\\s+");
                for (String p : parts) {
                    temps.add(Integer.parseInt(p));
                }
            }
        }

        if (currentChain != null) {
            result.add(build(currentChain, temps));
        }

        return result;
    }

    private static ChainTempDto build(int id, List<Integer> temps) {
        ChainTempDto c = new ChainTempDto();
        c.setChainId(id);
        c.setChipTemps(temps);

        return c;
    }


    public static String generateId(String ip) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom r = new SecureRandom(ip.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(r.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static Entry<String, String> getLocalIpAndMac() throws Exception {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("77.88.8.8"), 53);
            InetAddress localAddress = socket.getLocalAddress();
            String ip = localAddress.getHostAddress();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localAddress);

            if (ni == null)
                return new SimpleEntry<>(ip, "");

            byte[] macBytes = ni.getHardwareAddress();

            String mac = macBytes == null
                    ? null
                    : HexFormat.ofDelimiter(":").formatHex(macBytes);

            return new SimpleEntry<>(ip, mac);
        }
    }
}
