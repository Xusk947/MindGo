package mindgo.scene;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindgo.Main;
import mindgo.rooms.Room;
import mindgo.rooms.UnitRoom;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.world.Tile;

public class Shop extends Scene {
    public static final float WAITING_TIME = 60f * 30f; /* secs */
    public Game game;
    public Seq<Room> rooms;

    public Shop() {
        super();
        specialMap = "shop";
        rooms = new Seq<>();

        generateRoomData();
    }

    @Override
    public void update() {
        super.update();
        if (time > WAITING_TIME) Main.ME.goToScene(game);
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

    private void generateRoomData() {
        /*  */
    }
}
