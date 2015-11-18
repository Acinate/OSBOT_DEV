package WOODCUTTING;

/**
 * Created by Admin on 11/14/2015.
 */

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@ScriptManifest(name="Var_Oak Chopper", info="Chops Oaks Southwest VWest Bank", author="Acinate", version=1.0, logo="")
public class Varrock_Oaks extends Script {

    Area AREA_TREES = new Area(3171,3423,3159,3409);
    Area AREA_BANK = new Area(3187,3437,3180,3433);
    Entity tree, bankBooth;

    String status;
    long timeRan;
    long timeBegan;
    int startXP;
    int currentXP;
    int GainedXP;
    int xpPerHour;

    private enum State { LEVEL, CHOP, BANK, IDLE }

    private State getState() {
        if (dialogues.isPendingContinuation())
            return State.LEVEL;
        else if (AREA_TREES.contains(myPlayer()))
            return State.CHOP;
        else if (AREA_BANK.contains(myPlayer()))
            return State.BANK;
        return State.IDLE;
    }

    public void onStart() {
        this.status = "Activating GE Chopper";
        this.log("GE Chopper has initialized!");
        this.startXP = this.skills.getExperience(Skill.WOODCUTTING);
        this.timeBegan = System.currentTimeMillis();
    }

    public void onExit() {
        this.status = "Exiting GE Chopper..";
        this.log("GE Chopper has stopped!");
        this.log("Time ran: " + this.timeRan);
        this.log("XP Gained: " + this.GainedXP);
    }

    public int onLoop() throws InterruptedException {


        switch(getState()) {

            case LEVEL:
                dialogues.clickContinue();
                break;

            case CHOP:
                tree = objects.closest(AREA_TREES, "Oak");

                if (!inventory.isFull()) {
                    //CHOP
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving() && myPlayer().getInteracting() == null) {
                        if (tree.isVisible()) {
                            status = "Chopping down Tree";
                            tree.interact("Chop down");
                            for (int x = 0; x < 100 && (!myPlayer().isAnimating() || !myPlayer().isMoving()); x++) {
                                sleep(20);
                            }
                        } else camera.toEntity(tree);
                    } else if (myPlayer().isAnimating()) {
                        int x = random(1,10);
                        if(x == 5)
                            antiBan();
                    }
                } else if (inventory.isFull()) {
                    //BANK
                    status = "Walking to Bank";
                    while (!myPlayer().isMoving())
                        localWalker.walk(AREA_BANK, true);

                }
                break;

            case BANK:
                bankBooth = objects.closest(AREA_BANK, "Bank booth");

                if (inventory.isFull()) {
                    //BANK
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving() && myPlayer().getInteracting() == null) {
                        if (!bank.isOpen()) {
                            status = "Banking";
                            bankBooth.interact("Bank");
                            for (int x = 0; x < 100 && (!myPlayer().isAnimating() || !myPlayer().isMoving()); x++) {
                                sleep(20);
                            }
                        } else {
                            bank.depositAll();
                        }
                    } else if (myPlayer().isAnimating()) {

                    }
                } else if (!inventory.isFull()) {
                    //RUN TO TREES
                    status = "Walking to Trees";
                    while (!myPlayer().isMoving())
                        localWalker.walk(AREA_TREES, true);
                }
                break;

            case IDLE:

                if (inventory.isFull()) {
                    status = "LOST! Walking to bank!";
                    while (!myPlayer().isMoving())
                        localWalker.walk(AREA_BANK, true);
                } else {
                    status = "LOST! Walking to trees!";
                    while (!myPlayer().isMoving())
                        localWalker.walk(AREA_TREES, true);
                }

                break;
        }

        return 1000;
    }
    public void antiBan() throws InterruptedException {
        int random = random(1,10);

        for (int x = 0; x < 100 && myPlayer().isAnimating(); x++) {
            sleep(20);
        }

        if (random < 2) {
            status = "Checking Woodcutting XP";
            tabs.getSkills().hoverSkill(Skill.WOODCUTTING);
        }
        else if (random == 3) {
            status = "Checking Woodcutting XP";
            tabs.getSkills().hoverSkill(Skill.WOODCUTTING);
        }
        else if (random == 4) {
            status = "Checking Woodcutting XP";
            tabs.getSkills().hoverSkill(Skill.WOODCUTTING);
        }
        else if (random == 5) {
            status = "Fixing Camera";
            camera.toTop();
            camera.toEntity(tree);
        }
        else if (random > 5) {
            status = "AFK / Sleeping";
            tabs.open(Tab.INVENTORY);
            mouse.moveRandomly();
        }
    }
    public void onPaint(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 0, 14));
        this.timeRan = System.currentTimeMillis() - this.timeBegan;
        this.currentXP = this.skills.getExperience(Skill.WOODCUTTING);
        this.GainedXP = this.currentXP - this.startXP;
        this.xpPerHour = (int)((double)this.GainedXP / ((double)(System.currentTimeMillis() - this.timeBegan) / 3600000.0D));
        g.drawString("Status: " + this.status, 25, 280);
        g.drawString("Time Ran: " + this.ft(this.timeRan), 25, 295);
        g.drawString("XP Gained: " + this.GainedXP, 25, 310);
        g.drawString("XP Per hour:" + this.xpPerHour, 25, 325);
    }
    private String ft(long duration) {
        String res = "";
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        if(days == 0L) {
            res = hours + ":" + minutes + ":" + seconds;
        } else {
            res = days + ":" + hours + ":" + minutes + ":" + seconds;
        }

        return res;
    }

}

