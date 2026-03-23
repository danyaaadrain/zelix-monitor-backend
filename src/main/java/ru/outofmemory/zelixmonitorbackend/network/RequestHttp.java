package ru.outofmemory.zelixmonitorbackend.network;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.Socket;

@Log4j2
@Service
public class RequestHttp {
    public String getChipsTemp(String ip) {
        return this.send(ip, "miner_temp");
    }

    public String send(String ip, String url) {
        try (Socket socket = new Socket(ip, 6060)) {
            String request =
                    "GET /" + url + " HTTP/1.0\r\n" +
                            "Host: " + ip + "\r\n" +
                            "\r\n";

            socket.getOutputStream().write(request.getBytes());
            socket.getOutputStream().flush();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len;

            while ((len = socket.getInputStream().read(buf)) != -1) {
                baos.write(buf, 0, len);
            }

            String response = baos.toString();

            int bodyIndex = response.indexOf("\r\n\r\n");
            if (bodyIndex != -1) {
                return response.substring(bodyIndex + 4);
            }
            if (response.isEmpty()) {
                return null;
            }

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при запросе к HTTP. [http://" + ip + "/" + url + "]: " + e.getMessage());
        }
    }
}
