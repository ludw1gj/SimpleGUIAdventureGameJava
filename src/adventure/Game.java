package adventure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class Game {

    private final GUI gui = new GUI(new ChoiceHandler(), new TitleScreenHandler());

    private final Player player = new Player(15, "Knife");
    private boolean silverRing = false;
    private int goblinHP = 20;

    private Game() {
        gui.initializeStartScreen();
    }

    public static void main(String[] args) {
        new Game();
    }

    private void gameStart() {
        gui.updateCurrentHPLabel(player.getHP());
        gui.updateCurrentWeaponLabel(player.getWeapon());

        townGate();
    }

    private void updatePlayerHP(int hp, boolean isDamage) {
        if (isDamage) {
            player.receiveDamage(hp);
        } else {
            player.receiveHealing(hp);
        }
        gui.updateCurrentHPLabel(player.getHP());
    }

    private void updatePlayerWeapon(String weapon) {
        player.equipWeapon(weapon);
        gui.updateCurrentWeaponLabel(weapon);
    }

    private void townGate() {
        player.movePosition("townGate");

        gui.updateMainTextArea("You are at the gate of the town.\nA guard is standing in front of you.\n\nWhat do you do?");
        gui.updateChoiceButtons("Talk to the guard", "Attack the guard", "Leave", "");
    }

    private void talkGuard() {
        player.movePosition("talkGuard");

        gui.updateMainTextArea("Guard: Hello stranger. I have never seen your face.\nI'm sorry but we cannot let a stranger enter our town.");
        gui.updateChoiceButtonsNoActions();
    }

    private void attackGuard() {
        player.movePosition("attackGuard");

        gui.updateMainTextArea("Guard: Hey don't be stupid!!\n\nThe guard fought back and hit you hard.\n(You receive 3 damage)");
        gui.updateChoiceButtonsNoActions();

        updatePlayerHP(3, true);
    }

    private void crossRoad() {
        player.movePosition("crossRoad");

        gui.updateMainTextArea("You are at a cross road.\nIf you go south, you will go back to the town.");
        gui.updateChoiceButtons("Go North", "Go East", "Go South", "Go West");
    }

    private void north() {
        player.movePosition("north");

        gui.updateMainTextArea("There is a river.\nYou drink the water and rest and the riverside.\n(Your HP is recovered by 2)");
        gui.updateChoiceButtons("Go South", "", "", "");

        updatePlayerHP(2, false);
    }

    private void east() {
        player.movePosition("east");

        gui.updateMainTextArea("You walked into a forest and found a long sword!\n\n(You obtained a Long Sword)");
        gui.updateChoiceButtons("Go West", "", "", "");

        updatePlayerWeapon("Long Sword");
    }

    private void west() {
        player.movePosition("west");

        gui.updateMainTextArea("You encounter a goblin!");
        gui.updateChoiceButtons("Fight", "Run", "", "");
    }

    private void fight() {
        player.movePosition("fight");

        gui.updateMainTextArea("Goblin's HP: " + goblinHP + "\n\nWhat do you do?");
        gui.updateChoiceButtons("Fight", "Run", "", "");
    }

    private void attackGoblin() {
        player.movePosition("attackGoblin");

        int playerDamage = 0;
        if (player.getWeapon().equals("Knife")) {
            playerDamage = new java.util.Random().nextInt(3);
        } else if (player.getWeapon().equals("Long Sword")) {
            playerDamage = new java.util.Random().nextInt(10);
        }

        gui.updateMainTextArea("You attacked the goblin and gave " + playerDamage + " damage!");
        gui.updateChoiceButtonsNoActions();

        goblinHP -= playerDamage;
    }

    private void goblinAttacks() {
        player.movePosition("goblinAttacks");

        int goblinDamage = new java.util.Random().nextInt(6);

        gui.updateMainTextArea("The goblin attacked you and gave " + goblinDamage + " damage!");
        gui.updateChoiceButtonsNoActions();

        updatePlayerHP(goblinDamage, true);
    }

    private void killedGoblin() {
        player.movePosition("killedGoblin");

        gui.updateMainTextArea("You defeated the goblin!\nThe goblin dropped a ring!\n\n(You obtained a Silver Ring)");
        gui.updateChoiceButtons("Go East", "", "", "");

        silverRing = true;
    }

    private void death() {
        gui.updateMainTextArea("You are dead!\n\n<GAME OVER>");
        gui.hideChoiceButtons();
    }

    private void finishedGame() {
        gui.updateMainTextArea("Guard: Oh you killed that goblin?\nThank you so much. You are true hero!\nWelcome to our town!\n\n<THE END>");
        gui.hideChoiceButtons();
    }

    private class TitleScreenHandler implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            gui.createGameScreen();
            gameStart();
        }

    }

    private class ChoiceHandler implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            String yourChoice = actionEvent.getActionCommand();

            switch (player.getPosition()) {
                case "townGate":
                    switch (yourChoice) {
                        case "c1":
                            if (silverRing) {
                                finishedGame();
                            } else {
                                talkGuard();
                            }
                            break;
                        case "c2":
                            attackGuard();
                            break;
                        case "c3":
                            crossRoad();
                            break;
                    }
                    break;

                case "talkGuard":
                    switch (yourChoice) {
                        case "c1":
                            townGate();
                            break;
                    }
                    break;

                case "attackGuard":
                    switch (yourChoice) {
                        case "c1":
                            if (player.getHP() < 1) {
                                death();
                            } else {
                                townGate();
                            }
                            break;
                    }
                    break;

                case "crossRoad":
                    switch (yourChoice) {
                        case "c1":
                            north();
                            break;
                        case "c2":
                            east();
                            break;
                        case "c3":
                            townGate();
                            break;
                        case "c4":
                            west();
                            break;
                    }
                    break;

                case "north":
                    switch (yourChoice) {
                        case "c1":
                            crossRoad();
                            break;
                    }
                    break;

                case "east":
                    switch (yourChoice) {
                        case "c1":
                            crossRoad();
                            break;
                    }
                    break;

                case "west":
                    switch (yourChoice) {
                        case "c1":
                            fight();
                            break;
                        case "c2":
                            crossRoad();
                            break;
                    }
                    break;

                case "fight":
                    switch (yourChoice) {
                        case "c1":
                            attackGoblin();
                            break;
                        case "c2":
                            crossRoad();
                            break;
                    }
                    break;

                case "attackGoblin":
                    switch (yourChoice) {
                        case "c1":
                            if (goblinHP < 1) {
                                killedGoblin();
                            } else {
                                goblinAttacks();
                            }
                            break;
                    }
                    break;

                case "goblinAttacks":
                    switch (yourChoice) {
                        case "c1":
                            if (player.getHP() < 1) {
                                death();
                            } else {
                                fight();
                            }
                            break;
                    }
                    break;

                case "killedGoblin":
                    switch (yourChoice) {
                        case "c1":
                            crossRoad();
                            break;
                    }
                    break;
            }
        }

    }

}
