package ru.outofmemory.zelixmonitorbackend.network;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Service;
import ru.outofmemory.zelixmonitorbackend.miner.WebCredentials;
import tools.jackson.databind.ObjectMapper;

@Log4j2
@Service
@RequiredArgsConstructor
public class RequestCgi {
    private final ObjectMapper objectMapper;

    public <H> H fetchJson(String ip, String url, WebCredentials webCredentials, Class<H> clazz) {
        try {
            String raw = send(ip, url, webCredentials);
            if (raw == null || raw.isBlank()) {
                throw new RuntimeException("Пустой ответ от майнера [" + ip + "]");
            }

            return objectMapper.readValue(raw, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при десериализации ответа http://" + ip + "/cgi-bin/" + url + ".cgi в " + clazz.getName());
        }
    }

    protected String send(String ip, String url, WebCredentials auth) {
        BasicCredentialsProvider creds = new BasicCredentialsProvider();
        creds.setCredentials(
                new AuthScope(ip, 80),
                new UsernamePasswordCredentials(auth.getUsername(), auth.getPassword().toCharArray())
        );

        try (CloseableHttpClient client = HttpClients.custom()
                .setDefaultCredentialsProvider(creds)
                .build()) {
            HttpGet get = new HttpGet("http://" + ip + "/cgi-bin/" + url + ".cgi");

            try (ClassicHttpResponse resp = client.executeOpen(null, get, null)) {
                if (resp.getCode() != 200) {
                    throw new RuntimeException("HTTP " + resp.getCode());
                }

                return EntityUtils.toString(resp.getEntity());
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при запросе к CGI [" + ip + "]: " + e.getMessage());
        }
    }
}
