package AGILITY;

/**
 * Created by Admin on 11/13/2015.
 */

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@ScriptManifest(name="Canifis Course", author="Acinate", version=1.0, info="", logo="")
public class Canifis_Course extends Script {

    String status;
    long timeRan;
    long timeBegan;
    int startXP;
    int currentXP;
    int GainedXP;
    int xpPerHour;
    int numLaps = 0;

    //Global Delay Control
    int intervalSleep = 10;

    Area AREA_START = new Area(3505,3490,3511,3484);
    Area AREA_GAP = new Area(new Position(3504,3499,2), new Position(3508,3491,2));
    Area AREA_GAP2 = new Area(new Position(3496,3507,2), new Position(3504,3503,2));
    Area AREA_GAP3 = new Area(new Position(3493,3505,2), new Position(3485,3498,2));
    Area AREA_GAP4 = new Area(new Position(3480,3500,3), new Position(3474,3491,3));
    Area AREA_POLE = new Area(new Position(3484,3487,2), new Position(3477,3481,2));
    Area AREA_GAP5 = new Area(new Position(3487,3477,3), new Position(3504,3471,3));
    Area AREA_GAP6 = new Area(new Position(3509,3474,2), new Position(3517,3484,2));
    Area AREA_LAP = new Area(3512, 3484, 3506, 3486);
    Area AREA_FAIL1 = new Area(3481,3502,3484,3496);
    Area AREA_FAIL2 = new Area(3504,3478,3508,3473);

    public void onStart() {
        this.status = "Activating Canifis Course";
        this.log("Canifis Agility has initialized!");
        this.startXP = this.skills.getExperience(Skill.AGILITY);
        this.timeBegan = System.currentTimeMillis();
    }

    public void onExit() {
        this.status = "Exiting Canifis Agility..";
        this.log("Canifis Agility has stopped!");
        this.log("Time ran: " + this.timeRan);
        this.log("XP Gained: " + this.GainedXP);
    }

    private enum State {IDLE, START, GAP, GAP2, GAP3, GAP4, GAP5, GAP6, POLE, LAP, FAIL1, FAIL2}

    private State getState() {
        if (AREA_START.contains(myPlayer()))
            return State.START;
        else if (AREA_GAP.contains(myPlayer()))
            return State.GAP;
        else if (AREA_GAP2.contains(myPlayer()))
            return State.GAP2;
        else if (AREA_GAP3.contains(myPlayer()))
            return State.GAP3;
        else if (AREA_GAP4.contains(myPlayer()))
            return State.GAP4;
        else if (AREA_POLE.contains(myPlayer()))
            return State.POLE;
        else if (AREA_GAP5.contains(myPlayer()))
            return State.GAP5;
        else if (AREA_GAP6.contains(myPlayer()))
            return State.GAP6;
        else if (AREA_LAP.contains(myPlayer()))
            return State.LAP;
        else if (AREA_FAIL1.contains(myPlayer()))
            return State.FAIL1;
        else if (AREA_FAIL2.contains(myPlayer()))
            return State.FAIL2;
        return State.IDLE;
    }

    public int onLoop() throws InterruptedException {

        Entity treeBranch = objects.closest(AREA_START, "Tall tree");
        Entity roofGap1 = objects.closest(AREA_GAP, "Gap");
        Entity roofGap2 = objects.closest(AREA_GAP2, "Gap");
        Entity roofGap3 = objects.closest(AREA_GAP3, "Gap");
        Entity roofGap4 = objects.closest(AREA_GAP4, "Gap");
        Entity poleVault = objects.closest(AREA_POLE, 10831);
        Entity roofGap5 = objects.closest(AREA_GAP5, "Gap");
        Entity roofGap6 = objects.closest(AREA_GAP6, "Gap");

        switch(getState()) {
            case START:
                status = "Starting run";
                log("START");


                if (treeBranch != null && myPlayer().getInteracting() == null) {
                    if (!myPlayer().isMoving()) {
                        if (treeBranch.isVisible()) {
                            treeBranch.interact("Climb");
                            for (int i=0; i < 100 && !myPlayer().isAnimating(); i++) {
                                sleep(intervalSleep);
                            }
                        } else camera.toEntity(treeBranch);
                    }
                }
                break;
            case GAP:
                status = "Hopping Gap.";
                log("GAP1");

                GroundItem markOfGrace = groundItems.closest(AREA_GAP, 11849);

                if (markOfGrace != null) {
                    if (markOfGrace.isVisible()) {
                        markOfGrace.interact("Take");
                        for (int i=0; i < 100 && myPlayer().isMoving(); i++) {
                            sleep(20);
                        }
                    } else camera.toEntity(markOfGrace);
                }

                //Advance Obstacle Method
                if (roofGap1 != null && myPlayer().getInteracting() == null) {
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        if (roofGap1.isVisible()) {
                            status = "Hopping Gap..";
                            roofGap1.interact("Jump");
                        } else camera.toEntity(roofGap1);
                    }
                }
                break;
            case GAP2:
                status = "Hopping Gap.";
                log("GAP2");

                GroundItem markOfGrace2 = groundItems.closest(AREA_GAP2, 11849);

                if (markOfGrace2 != null) {
                    if (markOfGrace2.isVisible()) {
                        markOfGrace2.interact("Take");
                        for (int i=0; i < 100 && !myPlayer().isMoving(); i++) {
                            sleep(100);
                        }
                    } else camera.toEntity(markOfGrace2);
                }

                //Advance Obstacle Method
                if (roofGap2 != null && myPlayer().getInteracting() == null) {

                    camera.movePitch(42);
                    camera.moveYaw(90);

                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        if (roofGap2.isVisible()) {
                            status = "Hopping Gap..";
                            roofGap2.interact("Jump");
                        } else camera.toEntity(roofGap2);
                    }
                }
                break;

            case GAP3:
                log("GAP3");
                status = "Hopping Gap.";

                GroundItem markOfGrace3 = groundItems.closest(AREA_GAP3, 11849);

                if (markOfGrace3 != null) {
                    if (markOfGrace3.isVisible()) {
                        markOfGrace3.interact("Take");
                        for (int i=0; i < 100 && !myPlayer().isMoving(); i++) {
                            sleep(20);
                        }
                    } else camera.toEntity(markOfGrace3);
                }

                //Advance Obstacle Method
                if (roofGap3 != null && myPlayer().getInteracting() == null) {
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        if (roofGap3.isVisible()) {
                            status = "Hopping Gap..";
                            roofGap3.interact("Jump");
                        } else camera.toEntity(roofGap3);
                    }
                }
                break;

            case GAP4:
                status = "Hopping Gap.";
                log("GAP4");

                GroundItem markOfGrace4 = groundItems.closest(AREA_GAP4, 11849);

                if (markOfGrace4 != null) {
                    if (markOfGrace4.isVisible()) {
                        markOfGrace4.interact("Take");
                        for (int i=0; i < 100 && !myPlayer().isMoving(); i++) {
                            sleep(20);
                        }
                    } else camera.toEntity(markOfGrace4);
                }

                //Advance Obstacle Method
                if (roofGap4 != null && myPlayer().getInteracting() == null) {
                    camera.toEntity(roofGap4);
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        if (roofGap4.isVisible()) {
                            status = "Hopping Gap..";
                            roofGap4.interact("Jump");
                        } else camera.toEntity(roofGap4);
                    }
                }
                break;

            case POLE:
                status = "Pole Vaulting.";
                log("POLE");

                GroundItem markOfGraceP = groundItems.closest(AREA_POLE, 11849);

                if (markOfGraceP != null) {
                    camera.toEntity(poleVault);
                    if (markOfGraceP.isVisible()) {
                        markOfGraceP.interact("Take");
                        for (int i=0; i < 100 && !myPlayer().isMoving(); i++) {
                            sleep(20);
                        }
                    } else camera.toEntity(markOfGraceP);
                }

                //Advance Obstacle Method
                if (poleVault != null && myPlayer().getInteracting() == null) {

                    camera.movePitch(40);
                    camera.moveYaw(300);

                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        if (poleVault.isVisible()) {
                            status = "Pole Vaulting..";
                            poleVault.interact("Vault");
                        } else camera.toEntity(poleVault);
                    }
                }
                break;

            case GAP5:
                status = "Hopping Gap.";
                log("GAP5");

                GroundItem markOfGrace5 = groundItems.closest(AREA_GAP5, 11849);

                if (markOfGrace5 != null) {
                    if (markOfGrace5.isVisible()) {
                        markOfGrace5.interact("Take");
                        for (int i=0; i < 100 && !myPlayer().isMoving(); i++) {
                            sleep(20);
                        }
                    } else camera.toEntity(markOfGrace5);
                }

                //Advance Obstacle Method
                if (roofGap5 != null && myPlayer().getInteracting() == null) {
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        if (roofGap5.isVisible()) {
                            status = "Hopping Gap..";
                            roofGap5.interact("Jump");
                        } else camera.toEntity(roofGap5);
                    }
                }
                break;

            case GAP6:
                status = "Hopping Gap.";
                log("GAP6");

                GroundItem markOfGrace6 = groundItems.closest(AREA_GAP6, 11849);

                if (markOfGrace6 != null) {
                    if (markOfGrace6.isVisible()) {
                        markOfGrace6.interact("Take");
                        for (int i=0; i < 100 && !myPlayer().isMoving(); i++) {
                            sleep(20);
                        }
                    } else camera.toEntity(markOfGrace6);
                }

                //Advance Obstacle Method
                if (roofGap6 != null && myPlayer().getInteracting() == null) {

                    camera.movePitch(40);
                    camera.moveYaw(12);

                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        if (roofGap6.isVisible()) {
                            status = "Hopping Gap..";
                            roofGap6.interact("Jump");
                        } else camera.toEntity(roofGap6);
                    }
                }
                break;

            case LAP:
                Entity startBranch = objects.closest(AREA_START, "Tall tree");
                log("LAP");

                //Advance Obstacle Method
                if (startBranch != null && myPlayer().getInteracting() == null) {
                    if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
                        if (startBranch.isVisible()) {
                            startBranch.interact("Climb");
                        } else camera.toEntity(startBranch);
                    }
                }

                numLaps++;
                break;

            case FAIL1:
                log("FAIL1");
                status = "JAGEX'd.. Restarting";
                while (AREA_START.contains(myPlayer()) == false) {
                    localWalker.walk(AREA_START, true);
                    for (int i = 0; i < 100 && !myPlayer().isMoving(); i++) {
                        sleep(intervalSleep);
                    }
                }
                break;

            case FAIL2:
                log("FAIL2");
                status = "JAGEX'd.. Restarting";
                while (!AREA_START.contains(myPlayer())) {
                    localWalker.walk(AREA_START, true);
                    for (int i = 0; i < 100 && !myPlayer().isMoving(); i++) {
                        sleep(intervalSleep);
                    }
                }
                break;

            case IDLE:
                log("IDLE");
                antiBan();
                break;
        }
        return 1000;
    }

    public void antiBan() throws InterruptedException {
        int random = random(1,10);

        if (random < 2) {
            status = "Checking Agility XP";
            tabs.getSkills().hoverSkill(Skill.AGILITY);
        }
        else if (random == 3) {
            status = "Checking Agility XP";
            tabs.getSkills().hoverSkill(Skill.AGILITY);
        }
        else if (random == 4) {
            status = "Checking Agility XP";
            tabs.getSkills().hoverSkill(Skill.AGILITY);
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
        this.currentXP = this.skills.getExperience(Skill.AGILITY);
        this.GainedXP = this.currentXP - this.startXP;
        this.xpPerHour = (int)((double)this.GainedXP / ((double)(System.currentTimeMillis() - this.timeBegan) / 3600000.0D));
        g.drawString("Status: " + this.status, 25, 265);
        g.drawString("Time Ran: " + this.ft(this.timeRan), 25, 280);
        g.drawString("XP Gained: " + this.GainedXP, 25, 295);
        g.drawString("XP Per hour:" + this.xpPerHour, 25, 310);
        g.drawString("Laps Completed:" + this.numLaps, 25, 325);
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
