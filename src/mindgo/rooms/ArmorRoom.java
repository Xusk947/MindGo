package mindgo.rooms;

import mindgo.logic.PlayerData;

public class ArmorRoom extends CostRoom {
    public float armor;

    public ArmorRoom(float armor, int cost, int tx, int ty) {
        super(cost, tx, ty);
        this.armor = armor;
    }

    @Override
    public boolean canBuy(PlayerData pd) {
        return pd.data.armor < armor && pd.data.money > cost;
    }

    @Override
    public void onBuy(PlayerData pd) {
        pd.data.armor = armor;
    }
}
