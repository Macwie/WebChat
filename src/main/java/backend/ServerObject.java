package backend;

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
    protected boolean s_online;

    public ServerObject(int id, String name, String ip, String port, int current, String password, boolean s_public, boolean s_online) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.current = current;
        this.password = password;
        this.s_public = s_public;
        this.s_online = s_online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
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

    public void setS_public(boolean s_public) {
        this.s_public = s_public;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getProtect() {
        return protect;
    }

    public void setProtect(String protect) {
        this.protect = protect;
    }

    public boolean isS_online() {
        return s_online;
    }

    public void setS_online(boolean s_online) {
        this.s_online = s_online;
    }
}