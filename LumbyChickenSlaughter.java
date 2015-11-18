import org.osbot.rs07.antiban.AntiBan;
import org.osbot.rs07.api.NPCS;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;


/**
 * Created by Admin on 10/24/2015.
 */

@ScriptManifest(author = "Acinate", name="Chicken Killer",info="Kills Chicken north farm lumby",version=1.0,logo="")
public class LumbyChickenSlaughter extends Script {

    Area COOP = new Area(3169, 3307, 3186, 3288);
    Area CENTER = new Area(3176, 3297, 3180, 3293);
    int X_UP = 0, Y_UP = 0, X_DOWN = 0, Y_DOWN = 0;

    public void onStart() {
        log("Chicken Killer has initiated");
    }
    private enum State {KILL, LOOT, EAT, IDLE};

    private State getState() {
        while (!myPlayer().isAnimating())
            return State.KILL;
        if (2 == 1)
            return State.LOOT;
        if (3 == 1)
            return State.EAT;
        return State.IDLE;
        }

    public int onLoop() throws InterruptedException {

        switch(getState()) {
            case KILL:
                NPC Chicken = getNpcs().closest("Chicken");
                //5389
                if (CENTER.contains(myPlayer()) == false)
                    localWalker.walk(CENTER, true);

                if (myPlayer().isUnderAttack() == false && Chicken.isUnderAttack() == false) {
                    if (Chicken.getAnimation() != 5389) {
                        if (Chicken.isMoving()) {
                            Chicken.hover();
                            Chicken.interact("Attack");
                        } else {
                            for (int x = 0; x < 5; x++) {
                                if (x == 1)
                                    Chicken.interact("Attack");
                                else
                                    sleep(1000);
                            }
                        }
                    }
                }
                break;
            case LOOT:
                Area lootArea = new Area(X_UP, Y_UP, X_DOWN, Y_DOWN);
                log("case LOOT:");
                break;
            case EAT:
                log("case EAT:");
                break;
            case IDLE:
                log("case IDLE:");
                sleep(random(1000, 3500));
                int random = random(0,10);
                log(random);

                while (myPlayer().isAnimating()) {

                    if (random < 2)
                        tabs.getSkills().hoverSkill(Skill.DEFENCE);
                    else if (random == 3)
                        tabs.getSkills().hoverSkill(Skill.RANGED);
                    else if (random == 4)
                        tabs.getSkills().hoverSkill(Skill.HITPOINTS);
                    else if (random == 5)
                        tabs.getAntiBan().getBehavior(AntiBan.BehaviorType.CAMERA_SHIFT);
                    else if (random > 5) {
                        mouse.moveRandomly();
                        if (random == 6)
                            sleep(random(2000, 3000));
                        else if (random == 7)
                            sleep(random(4000, 6000));
                        else if (random == 8)
                            sleep(random(7500, 8800));
                        else if (random >= 9)
                            sleep(random(2500, 5421));
                    }
                }
                if (random % 2 == 0)
                tabs.open(Tab.INVENTORY);

                tabs.getAntiBan().getBehavior(AntiBan.BehaviorType.RANDOM_MOUSE_MOVEMENT);


                break;
        }
        return random(100, 200);
    }

    public void onExit() { log("Chicken Killer has exited"); }
}

