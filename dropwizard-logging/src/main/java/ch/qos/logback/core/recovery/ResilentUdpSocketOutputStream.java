package ch.qos.logback.core.recovery;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a resilent UDP connection as an {@link OutputStream}. If the client can't send
 * data to the server, it automatically tries to reconnect to it. Of course UDP is not
 * reliable by definition, by this allows to us to handle sporadic network hiccups and DNS issues.
 */
public class ResilentUdpSocketOutputStream extends ResilientOutputStreamBase {

    private final String host;
    private final int port;

    public ResilentUdpSocketOutputStream(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.os = openNewOutputStream();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create an UDP connection to " + host + ":" + port, e);
        }
        this.presumedClean = true;
    }

    @Override
    String getDescription() {
        return "udp [" + host + ":" + port + "]";
    }

    @Override
    OutputStream openNewOutputStream() throws IOException {
        return new SyslogOutputStream(host, port);
    }
}
