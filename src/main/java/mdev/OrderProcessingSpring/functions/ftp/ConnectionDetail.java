package mdev.OrderProcessingSpring.functions.ftp;

public class ConnectionDetail {

    private final String host, name, pass;
    private final int port;

    public ConnectionDetail(String host, int port, String name, String pass){
        this.host = host;
        this.pass = pass;
        this.name = name;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public int getPort() {
        return port;
    }
}
