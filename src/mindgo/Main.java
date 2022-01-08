package mindgo;

import arc.Events;
import arc.struct.Seq;
import arc.util.CommandHandler;
import mindgo.logic.PlayerData;
import mindgo.scene.Lobby;
import mindgo.scene.Scene;
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
        PlayerData.all = new Seq<>();
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
    }

    public void run() {
        goToScene(new Lobby());
        Vars.netServer.openServer();
    }

    public void eventsLoad() {
        // General update
        Events.on(EventType.Trigger.update.getClass(), (e) -> {
            // Firs-ting first update All player data now
            PlayerData.all.forEach(PlayerData::update);
            // Seconding second need update our current scene
            if (currentScene != null) {
                currentScene.update();
            }
        });
        // When player joined create PlayerData for them
        Events.on(EventType.PlayerJoin.class, (e) -> {
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
    }
}
