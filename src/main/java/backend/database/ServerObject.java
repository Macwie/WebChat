package backend.database;

import java.net.InetAddress;

public class ServerObject {
    protected int id;
    protected String name;
    protected String ip;
    protected String port;
    protected int current;
    protected String password;
    protected boolean s_public;
    protected String online;
    protected String protect;

    public ServerObject(int id, String name, String ip, String port, int current, String password, boolean s_public) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.current = current;
        this.password = password;
        this.s_public = s_public;
    }

    public boolean isOnline() {
        boolean online = false;
        try {
            InetAddress address = InetAddress.getByName(ip);
            if (!(address.isSiteLocalAddress() ||
                    address.isAnyLocalAddress() ||
                    address.isLinkLocalAddress() ||
                    address.isLoopbackAddress() ||
                    address.isMulticastAddress())) {
                if (address.isReachable(100))
                    online = true;
            }
        } catch (Exception e) {
            online = false;
        }
        return online;
    }

    public String getName() {
        return name;
    }
    
    public String getIp() {
        return ip;
    }
    
    public String getPort() {
        return port;
    }
    
    public String getPassword() {
        return password;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isS_public() {
        return s_public;
    }
    
    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
    
    public void setProtect(String protect) {
        this.protect = protect;
    }
}