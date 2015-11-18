/**
 * Created by Admin on 10/23/2015.
 */
import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@ScriptManifest(author = "Acinate", info = "Crafts Air Runes", name = "Air Crafter", version = 0, logo = "")

public class AirCrafter extends Script {


    int ess = 1436, laws = 563;
    Area ALTAR_AREA = new Area(2837,4839,2851,4826);
    Area RUINS_AREA = new Area(2982,3295,2988,3289);
    Area BANK_AREA = new Area(3009, 3358, 3015, 3353);
    Area BOOTH_AREA = new Area(3011,3356,3014,3355);
    Position[] WALK_ALTAR = new Position[]
            {
            new Position(3007,3348,0),
            new Position(3007,3342,0),
            new Position(3005,3334,0),
            new Position(3005,3327,0),
            new Position(3005,3321,0),
            new Position(2999,3316,0),
            new Position(2993,3310,0),
            new Position(2987,3305,0),
            new Position(2985,3300,0),
            new Position(2985,3296,0)
            };


    Position[] WALK_BANK = new Position[] {
            new Position(2971,3379,0),
            new Position(2979,3378,0),
            new Position(2987,3373,0),
            new Position(2992,3369,0),
            new Position(2998,3364,0),
            new Position(3005,3363,0),
            new Position(3010,3361,0),
            new Position(3012,3355,0),
    };

    public long timeBegan;
    public long timeRan;

    private int startXP, currentXP, GainedXP, xpPerHour;

    @Override
    public void onStart() {
        log("Let's get started!");
        timeBegan = System.currentTimeMillis();
        startXP = skills.getExperience(Skill.RUNECRAFTING);
    }

    private enum State {BANK, WALK_ALTAR, WALK_BANK, CRAFT, IDLE};

    private State getState(){
        if (BANK_AREA.contains(myPlayer()) && !inventory.contains(ess))
            return State.BANK;
        if (ALTAR_AREA.contains(myPlayer()))
            return State.CRAFT;
        if (!RUINS_AREA.contains(myPlayer()) && inventory.contains(ess))
            return State.WALK_ALTAR;
        if (!BANK_AREA.contains(myPlayer()) && !inventory.contains(ess))
            return State.WALK_BANK;
        return State.IDLE;
    }

    @Override
    public int onLoop() throws InterruptedException {

        switch(getState()) {
            case BANK:
                Entity bankBooth = getObjects().closest(BANK_AREA, 24101);

                bankBooth.hover();
                sleep(random(400, 500));
                bankBooth.interact("Bank");
                sleep(random(800,1000));

                if (bank.isOpen()) {
                    if (!bank.contains(563)) {
                        bot.getScriptExecutor().stop(true);
                    }

                    if (bank.contains(563) && !inventory.contains(laws)) {
                        bank.withdraw(563, 100);
                        sleep(random(101, 246));
                    }
                    if (bank.contains(1436) && !inventory.contains(ess))
                        bank.withdraw(1436, 100);
                    sleep(random(101, 246));
                    bank.close();
                }
                break;

            case WALK_ALTAR:
                Entity airRuins = getObjects().closest(RUINS_AREA, 14989);

                localWalker.walkPath(WALK_ALTAR);

                if (airRuins.isVisible()) {
                    airRuins.hover();
                    sleep(random(400, 789));
                    airRuins.interact("Enter");
                    sleep(random(2000,3000));
                } else camera.toEntity(airRuins);

                break;
            case CRAFT:
                Entity airAltar = getObjects().closest(ALTAR_AREA, 14897);

                if (inventory.contains(ess)) {
                    airAltar.hover();
                    sleep(random(101, 246));
                    airAltar.interact("Craft-rune");
                    sleep(random(2000, 3000));
                }
                if (!myPlayer().isAnimating()) {
                    magic.castSpell(Spells.NormalSpells.FALADOR_TELEPORT);
                    sleep(random(2000, 4000));
                } else tabs.skills.hoverSkill(Skill.RUNECRAFTING);

                break;
            case WALK_BANK:
                tabs.open(Tab.INVENTORY);

                localWalker.walkPath(WALK_BANK);
        }
        return random(200, 300);
    }

    @Override
    public void onExit() {
        log("Thanks for running my Air Crafter!");
    }

    @Override
    public void onPaint(Graphics2D g) {
        Graphics2D  gr = g;
        gr.setColor(Color.WHITE);
        gr.setFont(new Font("Arial", Font.ROMAN_BASELINE, 14));

        timeRan = System.currentTimeMillis() - timeBegan;
        currentXP = skills.getExperience(Skill.RUNECRAFTING);
        GainedXP = currentXP - startXP;
        xpPerHour = (int)(GainedXP / ((System.currentTimeMillis() - timeBegan) / 3600000.0));

        gr.drawString("Time Ran: " + ft(timeRan), 25, 297);
        gr.drawString("XP Gained: " + GainedXP, 25, 312);
        gr.drawString("XP Per hour:" + xpPerHour, 25, 327);
    }

    private String ft(long duration)
    {
        String res = "";
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                .toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                .toMinutes(duration));
        if (days == 0) {
            res = (hours + ":" + minutes + ":" + seconds);
        } else {
            res = (days + ":" + hours + ":" + minutes + ":" + seconds);
        }
        return res;
    }

}
