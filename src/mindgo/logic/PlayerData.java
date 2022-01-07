package mindgo.logic;

import arc.struct.Seq;
import arc.util.Time;
import mindustry.gen.Player;

public class PlayerData {

    public static Seq<PlayerData> all;

    static Seq<PlayerData> getAll() {
        if (all == null) all = new Seq<>();
        return all;
    }

    public Player player;
    public Data data;

    public PlayerData(Player player) {
        this.player = player;
        data = new Data();
    }

    public PlayerData(Player player, Data data) {
        this.player = player;
        this.data = data;
    }

    public void update() {
        data.time += Time.delta;
    }

    class Data {
        public float time;
        public int money;

        public Data() {
            this.time = 0;
            this.money = 0;
        }
    }
}
