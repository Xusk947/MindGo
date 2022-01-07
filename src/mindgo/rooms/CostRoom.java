package mindgo.rooms;

import mindgo.logic.PlayerData;
import mindustry.gen.Player;

public class CostRoom extends Room {
    public int cost;

    public CostRoom(int cost, int tx, int ty) {
        super(tx, ty);
        this.cost = cost;
    }

    @Override
    public void update(PlayerData pd) {
        Player player = pd.player;
        float mx = player.mouseX;
        float my = player.mouseY;

        if (canBuy(pd) && intersect(mx, my)) {
            onBuy(pd);
            pd.data.money -= cost;
        }
    }

    public String label() {
        return "[white]@CostRoom : [acccent]" + cost;
    }

    public boolean canBuy(PlayerData pd) {
        return pd.data.money >= cost;
    }
    public void onBuy(PlayerData pd) {

    }
}
