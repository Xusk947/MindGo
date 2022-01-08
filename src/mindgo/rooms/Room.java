package mindgo.rooms;

import mindgo.logic.PlayerData;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.world.Tiles;
import mindustry.world.blocks.environment.Floor;

public class Room {
    public final int SIZE = 2;
    public final int tx, ty;
    public final float drawx, drawy;
    // start pos : end pos
    final int stx, sty, etx, ety;
    final float sdx, sdy, edx, edy;

    public Room(int tx, int ty) {
        /* tile size */
        this.tx = tx;
        this.ty = ty;
        // corner value
        this.stx = tx - SIZE;
        this.sty = ty - SIZE;
        this.etx = tx + SIZE;
        this.ety = ty + SIZE;

        /* draw size */
        this.drawx = tx * Vars.tilesize;
        this.drawy = ty * Vars.tilesize;
        // corner value
        this.sdx = stx * Vars.tilesize;
        this.sdy = sty * Vars.tilesize;
        this.edx = etx * Vars.tilesize;
        this.edy = ety * Vars.tilesize;
    }

    public boolean intersect(int tx, int ty) {
        return (tx >= stx && ty >= sty && tx <= etx && ty <= ety);
    }

    public boolean intersect(float x, float y) {
        return (x >= sdx && y > sdy && x <= edx && y <= edy);
    }

    public void create() {
        Tiles tiles = Vars.world.tiles;

        for (int x = -SIZE; x < SIZE; x++) {
            for (int y = -SIZE; y < SIZE; y++) {
                tiles.get(tx - x, ty - y).setFloor((Floor) Blocks.darkPanel3);
                if (x == SIZE || x == -SIZE || y == SIZE || y == -SIZE) {
                    tiles.get(tx - x, ty - y).setBlock(Blocks.darkMetal);
                }
            }
        }
    };

    public void update(PlayerData data) { }
}
