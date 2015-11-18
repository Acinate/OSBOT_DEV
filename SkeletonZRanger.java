import org.osbot.rs07.antiban.AntiBan;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;

/**
 * Created by Admin on 10/24/2015.
 */
public class SkeletonZRanger extends Script {

    Area RiverSpot = new Area(3253,9911,3254,9910);
    Area Skeletons = new Area(3250,9917,3256,9914);

    public void onStart() {
        log("Thanks for choosing SkeletonZ!");
    }

    private enum State { KILL, IDLE, SAFEMODE };

    private State getState() {
        if (myPlayer().isAnimating() == false && RiverSpot.contains(myPlayer()) == true)
            return State.KILL;
        if (RiverSpot.contains(myPlayer()) == false)
            return State.SAFEMODE;
        return State.IDLE;
    }

    public int onLoop() throws InterruptedException {

        switch(getState()) {
            case KILL:
                log("case KILL:");
                NPC Skeleton = npcs.closest(Skeletons, "Skeleton");

                if (myPlayer().isUnderAttack() == false && Skeleton.isUnderAttack() == false) {
                    if (Skeleton.getAnimation() != 5389) {
                        if (Skeleton.isMoving()) {
                            Skeleton.hover();
                            Skeleton.interact("Attack");
                        } else {
                            for (int x = 0; x < 5; x++) {
                                if (x == 1)
                                    Skeleton.interact("Attack");
                                else
                                    sleep(1000);
                            }
                        }
                    }
                }

                break;
            case SAFEMODE:
                log("case SAFEMODE:");
                while (RiverSpot.contains(myPlayer()) == false)
                localWalker.walk(RiverSpot, true);
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

        return 5000;
    }

    public void onPaint(Graphics g) {
        Graphics2D  gr = (Graphics2D)g;

        gr.setColor(Color.WHITE);
        gr.setFont(new Font("Arial", Font.ROMAN_BASELINE, 10));

        gr.drawString("Time Running: ", 25, 50);

    }
}
