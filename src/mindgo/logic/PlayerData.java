package mindgo.logic;

import arc.struct.Seq;
import arc.util.Time;
import mindgo.items.Item;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.type.UnitType;

public class PlayerData {

    public static Seq<PlayerData> all;

    static Seq<PlayerData> getAll() {
        if (all == null) all = new Seq<>();
        return all;
    }

    public Player player;
    public Data data;
    public boolean seted;

    public PlayerData(Player player) {
        this.player = player;
        data = new Data();
    }

    public PlayerData(Player player, Data data) {
        this.player = player;
        this.data = data;
    }

    public void update() {

    }

    public class Data {
        public UnitType type;
        public float armor;
        public int money;
        public Team team;
        public Seq<Item> items;

        public Data() {
            this.type = UnitTypes.dagger;
            this.armor = 0;
            this.money = 0;
            this.items = new Seq<>();
        }
    }
}
