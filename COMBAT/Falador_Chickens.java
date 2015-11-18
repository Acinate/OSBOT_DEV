package COMBAT;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

/**
 * Created by Admin on 11/14/2015.
 */

@ScriptManifest(author="Acinate", name="Chicken Killer", info="Kills Chickens South Falador", logo="", version=1.0)
public class Falador_Chickens extends Script {

    Area chickenArea = new Area (3014, 3282, 3020, 3298);
    NPC Chicken;
    GroundItem Feather;
    private static final int featherID = 314;
    private static final String chickenID = "Chicken";

    public void onStart() { }

    public void onExit() {  }

    private enum State { LEVEL, ATTACK, LOOT, IDLE}

    private State getState() {
        if (dialogues.isPendingContinuation())
            return State.LEVEL;
        else if (!myPlayer().isAnimating())
            return State.ATTACK;
        return State.IDLE;
    }

    public int onLoop() {
        switch (getState()) {
            case LEVEL:
                dialogues.clickContinue();
                break;
            case ATTACK:
                Chicken = npcs.closest(chickenArea, chickenID);

                Feather = groundItems.closest(chickenArea, featherID);

                if (!myPlayer().isMoving() && Feather != null) {
                    Feather.interact("Take");
                }
                else if (!myPlayer().isMoving() && myPlayer().getInteracting() == null) {
                    if (Chicken.isVisible()) {
                        if (Chicken != null && Chicken.getAnimation() != 5389)
                            Chicken.interact("Attack");
                    } else camera.toEntity(Chicken);
                }

                break;
        }

        return 1000;
    }

}
