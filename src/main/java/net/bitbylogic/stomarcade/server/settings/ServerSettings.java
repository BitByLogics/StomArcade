package net.bitbylogic.stomarcade.server.settings;

import net.bitbylogic.kardia.server.KardiaServer;

public class ServerSettings {

    private boolean privateServer;

    private int maxPlayers;
    private KardiaServer.JoinState joinState;

    public ServerSettings() {
        this.privateServer = false;

        this.maxPlayers = 500;
        this.joinState = KardiaServer.JoinState.NOT_JOINABLE;
    }

    public boolean isPrivateServer() {
        return privateServer;
    }

    public int maxPlayers() {
        return maxPlayers;
    }

    public KardiaServer.JoinState joinState() {
        return joinState;
    }

    public void setJoinState(KardiaServer.JoinState joinState) {
        this.joinState = joinState;
    }

}
