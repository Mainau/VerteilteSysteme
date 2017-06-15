//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package aufg2c;

public class ServerConfig {
    private String hostname;
    private int receivePort;
    private int sendPort;
    private boolean available;

    public ServerConfig(String hostname, int receivePort, int sendPort) {
        this.hostname = hostname;
        this.receivePort = receivePort;
        this.sendPort = sendPort;
        this.available = true;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getReceivePort() {
        return this.receivePort;
    }

    public void setReceivePort(int port) {
        this.receivePort = port;
    }

    public int getSendPort() {
        return this.sendPort;
    }

    public void setSendPort(int port) {
        this.sendPort = port;
    }

    public String toString() {
        return "hostname: " + this.hostname + "; receive port: " + this.receivePort + "; send port: " + this.sendPort;
    }

    void bind() {
        this.available = false;
    }

    void release() {
        this.available = true;
    }

    boolean isAvailable() {
        return this.available;
    }
}
