package mindgo;

import arc.Events;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Log;
import mindgo.items.Items;
import mindgo.logic.PlayerData;
import mindgo.scene.Lobby;
import mindgo.scene.Scene;
import mindgo.scene.Shop;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Player;
import mindustry.mod.Plugin;

public class Main extends Plugin {

    // For Static use
    public static Main ME;

    public Scene currentScene;

    @Override
    public void init() {
        ME = this;
        Items.load();
        PlayerData.all = new Seq<>();
        PlayerData.map = new ObjectMap<>();
        eventsLoad();
        run();
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {

    }

    @Override
    public void registerServerCommands(CommandHandler handler) {

    }

    public void goToScene(Scene scene) {
        this.currentScene = scene;
        currentScene.worldLoad();
        Log.info("go to: " + scene);
    }

    public void run() {
        goToScene(new Shop());
        Vars.netServer.openServer();
    }

    public void eventsLoad() {
        // General update
        Events.on(EventType.Trigger.update.getClass(), (e) -> {
            if (currentScene != null) {
                // Firs-ting first update All player data now
                if (currentScene.needUpdatePlayers) {
                    PlayerData.all.forEach(PlayerData::update);
                }
                // Seconding second need update our current scene
                currentScene.update();
            }
        });
        // When player joined create PlayerData for them
        Events.on(EventType.PlayerConnect.class, (e) -> {
            PlayerData.add(e.player);
            if (currentScene != null) {
                currentScene.onPlayerJoin(e.player);
            }
        });
        // Load Scene Init
        Events.on(EventType.WorldLoadEvent.class, (e) -> {
            if (currentScene != null) {
                currentScene.onWorldLoad();
            }
        });

        Events.on(EventType.PlayerLeave.class, (e) -> {
            PlayerData.remove(e.player);
        });

        Events.on(EventType.TapEvent.class, (e) -> {
            if (currentScene != null) {
                currentScene.onPlayerTap(e.player, e.tile);
            }
        });

        Events.on(EventType.UnitDestroyEvent.class, (e) -> {
            if (currentScene != null && e.unit.isPlayer()) {
                currentScene.onPlayerDie(e.unit.getPlayer());
            }
        });
    }
}
