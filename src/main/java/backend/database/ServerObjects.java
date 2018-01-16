package backend.database;

import java.util.ArrayList;
import java.util.Iterator;

public class ServerObjects implements Iterable<ServerObject> {
    private ArrayList<ServerObject> serverList;

    public ServerObjects() {
        serverList = new ArrayList<ServerObject>();
    }

    public Iterator<ServerObject> publicIterator() {
        return new publicServerIterator();
    }

    public ArrayList<ServerObject> getServerList() {
        return serverList;
    }

    public void setServerList(ArrayList<ServerObject> serverList) {
        this.serverList = serverList;
    }

    @Override
    public Iterator<ServerObject> iterator() {
        return serverList.iterator();
    }

    private class publicServerIterator implements Iterator<ServerObject> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            if (serverList.size() == 0) {
                return false;
            } else if (serverList.size() == 1) {
                if (serverList.get(0).isS_public()) {
                    index++;
                    return true;
                } else {
                    return false;
                }
            } else if (index >= serverList.size()) {
                return false;
            } else {
                if (serverList.get(index).isS_public()) {
                    index++;
                    return true;
                } else {
                    index++;
                    return hasNext();
                }
            }
        }

        @Override
        public ServerObject next() {
            return serverList.get(index - 1);
        }
    }
}