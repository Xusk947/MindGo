package mindgo.scene;

import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Interval;
import mindgo.Main;
import mindgo.logic.PlayerData;
import mindgo.rooms.CostRoom;
import mindgo.rooms.Room;
import mindgo.rooms.UnitRoom;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.game.Teams;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.world.Tile;

public class Shop extends Scene {
    public static final float WAITING_TIME = 60f * 30f; /* secs */
    public Game game;
    public Seq<Room> rooms;

    Interval interval;
    ObjectMap<Integer, Player> red, blue, none;

    public Shop() {
        super();
        specialMap = "shop";
        rooms = new Seq<>();

        interval = new Interval();

        red = new ObjectMap<>();
        blue = new ObjectMap<>();
        none = new ObjectMap<>();

        needUpdatePlayers = true;

        generateRoomData();
    }

    @Override
    public void update() {
        if (Groups.player.size() < 1) return;
        super.update();
        boolean needUpdateHud = false;

        if (time > WAITING_TIME) {
            if (game == null) game = new Game();
            Main.ME.goToScene(game);
            // Sort all players who not select team
            for (Player player : none.values()) {
                if (red.size > blue.size) {
                    blue.put(player.id, player);
                } else if (blue.size > red.size) {
                    red.put(player.id, player);
                } else {
                    if (Mathf.random() > 0.5) {
                        red.put(player.id, player);
                    } else {
                        blue.put(player.id, player);
                    }
                }
            }
            // Assign Team in PlayerData.team
            for (Player player : blue.values()) {
                PlayerData.map.get(player.id).data.team = Team.blue;
            }

            for (Player player : red.values()) {
                PlayerData.map.get(player.id).data.team = Team.crux;
            }
            return;
        } else if (interval.get(60)) {
            needUpdateHud = true;
            for (Room room : rooms) {
                room.label();
            }
        };
        for (PlayerData pd : PlayerData.all) {
            Player player = pd.player;
            // Update HudText
            if (needUpdateHud) {
                String label = "[accent]" + ((int) (time / 60f) + " [white]: [accent]" + ((int) WAITING_TIME / 60f));
                // get block id for team selection
                int id = 0;
                if (player.x > 0 && player.unit() != null && player.unit().tileOn() != null) {
                    id = player.unit().tileOn().floorID();
                }
                // sand - red, grass - blue, not one of these - none
                if (id == Blocks.sand.id) {
                    if (red.size <= blue.size) {
                        // remove from none player when @Player not select team
                        if (none.containsKey(player.id)) {
                            none.remove(player.id);
                        }
                        // and put in #Red team
                        red.put(player.id, player);
                        label += " | [white] selected [red]#Red [white] team";
                    } else {
                        // if team is full say it to @Player
                        label += " | [red]#Red [white]team is full";
                    }
                } else if (id == Blocks.grass.id) /* Add this code for blue team too */ {
                    if (blue.size <= red.size) {
                        if (none.containsKey(player.id)) {
                            none.remove(player.id);
                        }
                        blue.put(player.id, player);
                        label += " | [white] selected [blue]#Blue [white] team";
                    } else {
                        // if team is full say it to @Player
                        label += " | [blue]#Blue [white]team is full";
                    }
                } else /* if player out of Team block put @Player in none team */ {
                    if (red.containsKey(player.id)) {
                        red.remove(player.id);
                    } else if (blue.containsKey(player.id)) {
                        blue.remove(player.id);
                    }
                    label += " | [white] select your team!";
                }

                label += "\n[white]To [accent]select [white]a team stand on a block of [blue]@Grass or [red]@Sand";
                // and finnaly send data
                if (player.team().core() != null) {
                    Call.label(player.con, label, 1f, player.team().core().x, player.team().core().y);
                }
            }
            // Shop logic
            if (pd.data.type != player.unit().type && pd.player.team().core() != null) {
                Unit unit = pd.data.type.create(pd.player.team());
                unit.set(unit.team().core().x, unit.team().core().y + Vars.tilesize * (unit.team().core().block.size));
                unit.add();
                pd.player.unit().spawnedByCore = true;
                pd.player.unit(unit);
            }
        }
    }

    @Override
    public void onPlayerJoin(Player player) {
        if (game != null && game.inGame.containsKey(player.id)) {
            PlayerData.map.get(player.id).data = game.inGame.get(player.id).data;
        }
    }

    @Override
    public void onWorldLoad() {
        super.onWorldLoad();
        PlayerData.all.forEach(pd -> {
            pd.player.team(Team.sharded);
            pd.data.team = Team.sharded;
        });
    }

    @Override
    public void customGenerate() {
        for (Tile tile : Vars.world.tiles) {
            final short id = tile.floorID();
            if (id == Blocks.darkPanel1.id) {
                /* Dark Panel 1 | Unit Dagger | Class Balanced */
                rooms.add(new UnitRoom(UnitTypes.dagger, 100, tile.x, tile.y));
            } else if (id == Blocks.darkPanel2.id) {
                /* Dark Panel 2 | Unit Mace | Class Rusher */
                rooms.add(new UnitRoom(UnitTypes.mace, 300, tile.x, tile.y));
            } else if (id == Blocks.darkPanel4.id) {
                /* Dark Panel 4 | Unit Nove | Class Sniper */
                rooms.add(new UnitRoom(UnitTypes.nova, 250, tile.x, tile.y));
            } else if (id == Blocks.darkPanel5.id) {
                /* Dark Panel 5 | Unit | */
            }
        }
        // Fill rooms with blocks
        rooms.forEach(Room::create);
    }

    @Override
    public void onPlayerTap(Player player, Tile tile) {
        for (Room room : rooms) {
            room.update(PlayerData.map.get(player.id));
        }

        if (player.unit() != null && player.unit().tileOn() != null) {
            PlayerData pd = PlayerData.map.get(player.id);
            pd.clickX = player.mouseX;
            pd.clickY = player.mouseY;
        }
    }

    private void generateRoomData() {
        /*  */
    }
}
