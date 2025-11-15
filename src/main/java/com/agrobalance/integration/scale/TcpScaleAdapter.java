package com.agrobalance.integration.scale;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class TcpScaleAdapter implements ScalePort {
    private final ScaleProperties props;

    @Override
    public ScaleReading readStableWeight() {
        if (props.getTcp() == null || props.getTcp().getHost() == null || props.getTcp().getHost().isBlank()) {
            throw new IllegalStateException("Host TCP da balança não configurado (scale.tcp.host).");
        }
        if (props.getParseRegex() == null || props.getParseRegex().isBlank()) {
            throw new IllegalStateException("Configuração de regex de parsing ausente (scale.parse-regex).");
        }

        var tcp = props.getTcp();
        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(tcp.getHost(), tcp.getPort()), tcp.getConnectTimeoutMillis());
            s.setSoTimeout(tcp.getReadTimeoutMillis());
            InputStream in = s.getInputStream();

            // Leitura mais robusta: tenta até ter algum conteúdo ou atingir timeout do socket
            byte[] buf = in.readNBytes(256);
            String str = new String(buf, StandardCharsets.US_ASCII).trim();

            var m = Pattern.compile(props.getParseRegex()).matcher(str);
            if (m.find()) {
                double v = Double.parseDouble(m.group("value"));
                return new ScaleReading(v, props.getUnit(), true, Instant.now());
            }
            return new ScaleReading(0, props.getUnit(), false, Instant.now());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler balança via TCP (" + tcp.getHost() + ":" + tcp.getPort() + ")", e);
        }
    }
}