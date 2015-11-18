package COMBAT;

/**
 * Created by Admin on 11/14/2015.
 */

import org.osbot.rs07.api.NPCS;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@ScriptManifest(author="Acinate", name="Cow Mager", info="Kills Cows South Falador", logo="", version=1.0)
public class Falador_Cows_Mager extends Script {

    Area AREA_COWS = new Area(3021, 3313,3043,3297);
    Area AREA_PLAYER;
    NPC cow, cowCalf, nearestCow, NearestCowCalf;

    String status;
    long timeRan;
    long timeBegan;
    int startXP;
    int currentXP;
    int GainedXP;
    int xpPerHour;

    public void onStart() {
        this.status = "Activating Cow Killer";
        this.log("Cow Killer has initialized!");
        this.startXP = this.skills.getExperience(Skill.MAGIC) + this.skills.getExperience(Skill.HITPOINTS);
        this.timeBegan = System.currentTimeMillis();
    }

    public void onExit() {
        this.status = "Exiting Cow Killer..";
        this.log("Cow Killer has stopped!");
        this.log("Time ran: " + this.timeRan);
        this.log("XP Gained: " + this.GainedXP);
    }

    private enum State { LEVEL, ATTACK, IDLE }

    private State getState() {
        if (!myPlayer().isAnimating())
            return State.ATTACK;
        return State.IDLE;
    }

    public int onLoop() throws InterruptedException {

        switch(getState()) {
            case ATTACK:
                cow = npcs.closest(AREA_COWS, "Cow");
                cowCalf = npcs.closest(AREA_COWS, "Cow calf");
                AREA_PLAYER = new Area(myPlayer().getX() + 4, myPlayer().getY() + 4, myPlayer().getX() - 4, myPlayer().getY() - 4);
                nearestCow = npcs.closest(AREA_PLAYER, "Cow");
                NearestCowCalf = npcs.closest(AREA_PLAYER, "Cow calf");

                if (nearestCow != null) {
                    status = "Attacking Nearest Cow";
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving() && myPlayer().getInteracting() == null) {
                        if (nearestCow.isVisible()) {
                            status = "Attacking Cow";
                            nearestCow.interact("Attack");
                            for (int x = 0; x < 100 && (!myPlayer().isAnimating() || !myPlayer().isMoving()); x++) {
                                sleep(20);
                            }
                        } else camera.toEntity(nearestCow);
                    }
                } else if (NearestCowCalf != null) {
                    status = "Attacking Nearest Cow Calf";
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving() && myPlayer().getInteracting() == null) {
                        if (NearestCowCalf.isVisible()) {
                            status = "Attacking Cow calf";
                            NearestCowCalf.interact("Attack");
                            for (int x = 0; x < 100 && (!myPlayer().isAnimating() || !myPlayer().isMoving()); x++) {
                                sleep(20);
                            }
                            antiBan();
                        } else camera.toEntity(NearestCowCalf);
                    }
                } else if (cow != null) {
                    status = "Attacking Cow";
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving() && myPlayer().getInteracting() == null) {
                        if (cow.isVisible()) {
                            status = "Attacking Cow";
                            cow.interact("Attack");
                            for (int x = 0; x < 100 && (!myPlayer().isAnimating() || !myPlayer().isMoving()); x++) {
                                sleep(20);
                            }
                        } else camera.toEntity(cow);
                    }
                } else if (cowCalf != null) {
                    status = "Attacking Cow Calf";
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving() && myPlayer().getInteracting() == null) {
                        if (cowCalf.isVisible()) {
                            status = "Attacking Cow calf";
                            cowCalf.interact("Attack");
                            for (int x = 0; x < 100 && (!myPlayer().isAnimating() || !myPlayer().isMoving()); x++) {
                                sleep(20);
                            }
                            antiBan();
                        } else camera.toEntity(cowCalf);
                    }
                }
                break;
            case IDLE:

                int r = random(1,10);
                if (r == 5)
                    antiBan();
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
            status = "Checking Magic XP";
            tabs.getSkills().hoverSkill(Skill.MAGIC);
        }
        else if (random == 3) {
            status = "Checking Magic XP";
            tabs.getSkills().hoverSkill(Skill.MAGIC);
        }
        else if (random == 4) {
            status = "Checking HP XP";
            tabs.getSkills().hoverSkill(Skill.HITPOINTS);
        }
        else if (random == 5) {
            status = "Fixing Camera";
            camera.toTop();
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
        this.currentXP = this.skills.getExperience(Skill.MAGIC) + this.skills.getExperience(Skill.HITPOINTS);
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
