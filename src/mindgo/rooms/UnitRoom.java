package mindgo.rooms;

import mindgo.logic.PlayerData;
import mindustry.type.UnitType;

public class UnitRoom extends CostRoom {

    public UnitType type;

    public UnitRoom(UnitType type, int cost, int tx, int ty) {
        super(cost, tx, ty);
        this.type = type;
    }

    @Override
    public boolean canBuy(PlayerData pd) {
        return pd.data.type != type && pd.data.money >= cost;
    }

    @Override
    public void onBuy(PlayerData pd) {
        pd.data.type = type;
    }
}
