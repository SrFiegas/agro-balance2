package com.agrobalance.integration.scale;

import com.fazecast.jSerialComm.SerialPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class SerialScaleAdapter implements ScalePort {
    private final ScaleProperties props;

    @Override
    public ScaleReading readStableWeight() {
        var serialProps = props.getSerial();
        if (serialProps == null || serialProps.getPort() == null || serialProps.getPort().isBlank()) {
            throw new IllegalStateException("Configuração de porta serial ausente (scale.serial.port).");
        }
        if (props.getParseRegex() == null || props.getParseRegex().isBlank()) {
            throw new IllegalStateException("Configuração de regex de parsing ausente (scale.parse-regex).");
        }

        SerialPort p = SerialPort.getCommPort(serialProps.getPort());
        int parity = switch (String.valueOf(serialProps.getParity()).toUpperCase()) {
            case "ODD" -> SerialPort.ODD_PARITY;
            case "EVEN" -> SerialPort.EVEN_PARITY;
            case "MARK" -> SerialPort.MARK_PARITY;
            case "SPACE" -> SerialPort.SPACE_PARITY;
            default -> SerialPort.NO_PARITY;
        };
        p.setComPortParameters(
                serialProps.getBaudRate(),
                serialProps.getDataBits(),
                serialProps.getStopBits(),
                parity
        );
        p.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, serialProps.getReadTimeoutMillis(), 0);

        if (!p.openPort()) {
            throw new RuntimeException("Falha ao abrir porta serial: " + serialProps.getPort());
        }
        try {
            byte[] buf = new byte[256];
            int n = p.readBytes(buf, buf.length);
            String s = new String(buf, 0, Math.max(0, n), StandardCharsets.US_ASCII).trim();
            Pattern r = Pattern.compile(props.getParseRegex());
            Matcher m = r.matcher(s);
            if (m.find()) {
                double v = Double.parseDouble(m.group("value"));
                return new ScaleReading(v, props.getUnit(), true, Instant.now());
            }
            return new ScaleReading(0, props.getUnit(), false, Instant.now());
        } finally {
            try {
                p.closePort();
            } catch (Exception ignored) {
            }
        }
    }
}