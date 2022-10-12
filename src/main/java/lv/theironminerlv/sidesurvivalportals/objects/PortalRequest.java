package lv.theironminerlv.sidesurvivalportals.objects;

public class PortalRequest {

    private final String player;
    private final String uuid;
    private final String portalId;
    private boolean safe;

    public PortalRequest(String player, String uuid, String portalId) {
        this.player = player;
        this.uuid = uuid;
        this.portalId = portalId;
    }

    public PortalRequest(String player, String uuid, String portalId, boolean safe) {
        this.player = player;
        this.uuid = uuid;
        this.portalId = portalId;
        this.safe = safe;
    }

    public String getPlayer() {
        return player;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPortalId() {
        return portalId;
    }

    public boolean isSafe() {
        return safe;
    }
}
