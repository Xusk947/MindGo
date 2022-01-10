package mindgo.logic;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Log;
import arc.util.Time;
import mindgo.items.Item;
import mindgo.items.ItemStack;
import mindgo.items.Items;
import mindustry.content.Fx;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.type.UnitType;

public class PlayerData {

    public static Seq<PlayerData> all;
    public static ObjectMap<Integer, PlayerData> map;

    public Player player;
    public Data data;

    public float clickX, clickY;
    public float oldMouseX, oldMouseY;
    public boolean seted;

    public float lastHealth;

    public boolean needHudUpdate;
    public boolean workWithBomb;
    public boolean wantBuy;

    boolean readyForItemUse;
    float itemUseTimer;
    Interval interval;

    public static void add(Player player) {
        PlayerData pd = new PlayerData(player);
        all.add(pd);
        map.put(player.id, pd);
    }

    public static void remove(Player player) {
        PlayerData pd = map.get(player.id);
        all.remove(pd);
        map.remove(player.id);
    }

    public static void reset() {
        for (PlayerData pd : all) {
            pd.data = new Data();
        }
    }

    public PlayerData(Player player) {
        this.player = player;
        data = new Data();
        interval = new Interval(2);
    }

    public PlayerData(Player player, Data data) {
        this.player = player;
        this.data = data;
        interval = new Interval(2);
    }

    public void update() {
        if (player.unit() != null && player.unit().type != null) {
            if (player.unit().health != lastHealth) {
                lastHealth = player.unit().health;
                needHudUpdate = true;
            }
            if (!workWithBomb && player.unit().ammo < 0.1f && data.ammo > 0) {
                data.ammo--;
                player.unit().ammo = player.unit().type.ammoCapacity * 1.5f;
                needHudUpdate = true;
            }
            if (player.unit().armor != data.armor) {
                data.armor = player.unit().armor;
                needHudUpdate = true;
            }

            itemUseTimer -= Time.delta;
            if (interval.get(1, 5) && itemUseTimer > 0) {
                needHudUpdate = true;
            }
            if (clickX > -100) {
                float clickDst2 = player.dst2(clickX, clickY);
                float mouseDst2 = player.dst2(player.mouseX, player.mouseY);

                if (clickDst2 < 576) {
                    switchItem(player.mouseX > player.x);
                    Call.label(player.con, getItem().item.icon + "", 1.5f, player.mouseX, player.mouseY);
                    readyForItemUse = false;
                } else if (!readyForItemUse && itemUseTimer < 0 && clickDst2 < 3136) {
                    Call.label(player.con, getItem().item.icon + " ready for use!", 1.5f, player.mouseX, player.mouseY);
                    readyForItemUse = true;
                    needHudUpdate = true;
                }
                clickX = -200;
            } else if (readyForItemUse) {
                if (player.shooting()) {
                    if (interval.get(0, 5)) {
                        Call.effect(player.con, Fx.bubble, player.mouseX, player.mouseY, 0, player.team().color);
                    }
                } else {
                    readyForItemUse = false;
                    useItem();
                    itemUseTimer = 60f * 3f; // 3 secs cooldown
                }
            }
            oldMouseX = player.mouseX;
            oldMouseY = player.mouseY;
        }

        if (needHudUpdate) {
            StringBuilder label = new StringBuilder("[red]" + ((int) (player.unit().health / player.unit().type.health * 100)) + "%");
            label.append(" [white]").append((int) data.armor).append(Iconc.statusShielded);
            label.append(" [white]| money: ").append(data.money).append(Iconc.itemSurgeAlloy);
            label.append(" | ammo: ").append(data.ammo).append("\n");
            Seq<ItemStack> stacks = data.items.values().toSeq();

            for (int i = 0; i < stacks.size; i++) {
                // color selected item to green color else in white
                if (i == data.currentItem) {
                    label.append("[green]");
                } else {
                    label.append("[white]");
                }
                ItemStack is = stacks.get(i);
                if (is.item != Items.nil && is.count > 0) {
                    label.append(" | ").append(is.item.icon).append("x").append(is.count);
                } else {
                    label.append(" | ").append(is.item.icon);
                }
            }

            Call.setHudText(player.con, label.toString());
            needHudUpdate = false;
        }
    }

    public ItemStack getItem() {
        if (!data.items.isEmpty()) {
            return data.items.values().toSeq().get(data.currentItem);
        }
        return new ItemStack(Items.nil);
    }

    public void switchItem(boolean next) {
        if (next) {
            data.currentItem++;
            if (data.currentItem >= data.items.size) {
                data.currentItem = 0;
            }
        } else {
            data.currentItem--;
            if (data.currentItem <= 0) {
                data.currentItem = data.items.size - 1;
            }
        }
        needHudUpdate = true;
    }

    public void useItem() {
        ItemStack is = getItem();
        if (!is.empty()) {
            is.useItem(this);
            needHudUpdate = true;
        } else {
            data.currentItem = 0;
            data.items.remove(is.item);
        };
    }

    public static class Data {
        public UnitType type;
        public Unit unit;
        public float armor;
        public int money;
        public Team team;
        public ObjectMap<Item, ItemStack> items;
        public int currentItem = 0;
        public int ammo;

        public Data() {
            this.type = UnitTypes.dagger;
            this.armor = 0;
            this.money = 99999;
            this.ammo = 10;
            this.items = new ObjectMap<>();

            items.put(Items.nil, new ItemStack(Items.nil));
            ItemStack is = new ItemStack(Items.grenade);
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            is.add();
            items.put(Items.grenade, is);
        }
    }
}
