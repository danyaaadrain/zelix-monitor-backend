package ru.outofmemory.zelixmonitorbackend.network;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class RequestSocket {

    private final ObjectMapper objectMapper;

    public <H> H fetchJson(String ip, String command, boolean newApi, Class<H> clazz) {
        Map<String, Object> cmd = new HashMap<>();
        cmd.put("command", command);
        cmd.put("new_api", newApi);

        String rawResponse = send(ip, cmd);

        try {
            return objectMapper.readValue(rawResponse, clazz);
        } catch (Exception e) {
            log.warn("[SOCKET] [{}:4028] Ошибка при десериализации: {} -> {}", ip, cmd, clazz.getSimpleName(), e);
            throw new RuntimeException("Ошибка десериализации ответа сокета", e);
        }
    }

    protected String send(String ip, Map<String, Object> command) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, 4028), 5000);
            socket.setSoTimeout(5000);

            String jsonCommand = objectMapper.writeValueAsString(command);
            socket.getOutputStream().write(jsonCommand.getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();

            String reply = new String(socket.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();

            if (!reply.contains("\"STATUS\": \"S\"") && !reply.contains("\"STATUS\":\"S\"")) {
                log.warn("[SOCKET] [{}:4028] Статус ответа не S: {}", ip, reply);
            }

            return reply;
        } catch (IOException e) {
            log.warn("[SOCKET] Ошибка при запросе к сокету [{}:4028]: {}", ip, e.getMessage());
            throw new UncheckedIOException("Ошибка при подключении к сокету", e);
        }
    }
}
