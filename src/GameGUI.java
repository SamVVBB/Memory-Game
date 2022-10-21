import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameGUI {

    private int score, attempts;
//    private Font font = new Font("Dialog", Font.BOLD, 40);
    private Font font = new Font("Dialog", Font.BOLD, 20);
    private JFrame frame;
    private JLabel scoreLabel, attemptsLabel;
    private JPanel infoPanel, cardPanel;

    // CARDS
    private String carBackPath = "src\\resources\\Tarot(CardBack).png";
    private String sunCardPath = "src\\resources\\theSunCard.png";
    private String towerCardPath = "src\\resources\\theTowerCard.png";
    private String testCardPath2 = "src\\resources\\testTWO.png";
    private String testCardPath3 = "src\\resources\\testTHREE.png";
    private String testCardPath4 = "src\\resources\\testFOUR.png";
    private String testCardPath5 = "src\\resources\\testFIVE.png";
    private String testCardPath6 = "src\\resources\\testSIX.png";
    private String testCardPath7 = "src\\resources\\testSEVEN.png";
    private String testCardPath8 = "src\\resources\\testEIGHT.png";
    private String testCardPath9 = "src\\resources\\testNINE.png";
    private Card[] set = new Card[10];
    private Card sunCard = new Card(carBackPath, sunCardPath);
    private Card towerCard = new Card(carBackPath, towerCardPath);
    private Card testCard2 = new Card(carBackPath, testCardPath2);
    private Card testCard3 = new Card(carBackPath, testCardPath3);
    private Card testCard4 = new Card(carBackPath, testCardPath4);
    private Card testCard5 = new Card(carBackPath, testCardPath5);
    private Card testCard6 = new Card(carBackPath, testCardPath6);
    private Card testCard7 = new Card(carBackPath, testCardPath7);
    private Card testCard8 = new Card(carBackPath, testCardPath8);
    private Card testCard9 = new Card(carBackPath, testCardPath9);
    private Card[] gameCards;

    // AUDIO
    private File music = new File("src\\resources\\mysterious-music.wav");
    private AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music);
    private Clip clip;

    GameGUI() throws IOException, LineUnavailableException, UnsupportedAudioFileException {

        // AUDIO
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);

        // INFO
        score = 0;
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(font);
        scoreLabel.setForeground(Color.WHITE);

        attempts = 30;
        attemptsLabel = new JLabel("Attempts: 30");
        attemptsLabel.setFont(font);
        attemptsLabel.setForeground(Color.WHITE);

        infoPanel = new JPanel();
//        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 450, 30, 30));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(6, 200, 4, 30));
//        infoPanel.setLayout(new GridLayout(2, 1, 0, 20));
        infoPanel.setLayout(new GridLayout(1, 2, 0, 4));
        infoPanel.setBackground(Color.BLACK);
        infoPanel.add(scoreLabel);
        infoPanel.add(attemptsLabel);

        // CARD SET
        set[0] = sunCard;
        set[1] = towerCard;
        set[2] = testCard2;
        set[3] = testCard3;
        set[4] = testCard4;
        set[5] = testCard5;
        set[6] = testCard6;
        set[7] = testCard7;
        set[8] = testCard8;
        set[9] = testCard9;

        // CARDS IN PLAY
        gameCards = new Card[20];
        for (int i = 0; i < set.length; i++) {
            gameCards[i] = set[i];
            gameCards[i].setIcon(set[i].getCardBack());
            gameCards[i + 10] = gameCards[i].makeCopy();
            gameCards[i + 10].setIcon(set[i].getCardBack());
        }

        // SET UP
        shuffle();
        assignMouseListener();

        // CARD PANEL
        cardPanel = new JPanel();
//        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        cardPanel.setLayout(new GridLayout(4,5,60,5));
        cardPanel.setLayout(new GridLayout(4,5,10,5));
        cardPanel.setBackground(Color.BLACK);

        for(int i = 0; i < gameCards.length; i++) {
            cardPanel.add(gameCards[i]);
        }

        // FRAME
        frame = new JFrame();
        frame.add(cardPanel, BorderLayout.CENTER);
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Memory Game");
        frame.pack();
//        frame.setSize(1100, 1240);
        frame.setSize(840, 1080);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void shuffle() {
        List<Card> shuffleList = Arrays.asList(gameCards);
        Collections.shuffle(shuffleList);
        shuffleList.toArray(gameCards);
    }

    private void assignMouseListener() {
        for (int i = 0; i < gameCards.length; i++) {
            int finalI = i;
            gameCards[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (checkIfTwoCardsMatch()) {
                        score++;
                        scoreLabel.setText("Score: " + score);
                        removePair();

                        // continue game
                        if (allCardsRemoved()) {
                            reactivateCards();
                        }

                    } else if (checkIfTwoCardsFaceUp()){
                        attempts--;
                        if (attempts <= 0) {
                            gameOver();
                        } else {
                            attemptsLabel.setText("Attempts: " + attempts);
                            faceAllCardsDown();
                        }
                    }
                    if (gameCards[finalI].getIcon() == gameCards[finalI].getCardBack()) {
                        gameCards[finalI].setIcon(gameCards[finalI].getCardImage());
                        gameCards[finalI].flipCard();
                    }
                }
            });
        }
    }

    private boolean checkIfTwoCardsMatch() {
        Icon icon1 = null;
        for (Card card : gameCards) {
            if (card.isFaceUp()) {
                if (card.getCardImage() == icon1) {
                    return true;
                }
                icon1 = card.getCardImage();
                continue;
            }
        }
        return false;
    }

    private boolean checkIfTwoCardsFaceUp() {
        int count = 0;
        for (Card card : gameCards) {
            if (count == 2) {
                return true;
            }
            if (card.isFaceUp()) {
                count++;
                continue;
            }
        }
        return false;
    }

    private boolean allCardsRemoved() {
        boolean allGone = true;
        for (Card card : gameCards) {
            if (!card.isRemoved()) {
                allGone = false;
                break;
            }
        }
        return allGone;
    }

    private void faceAllCardsDown() {
        for (Card card : gameCards) {
            if (card.isFaceUp()) {
                card.setIcon(card.getCardBack());
                card.flipCard();
            }
        }
    }

    private void removePair() {
        // scan for more than two flipped over bug
        int numFaceUp = 0;
        for (Card card : gameCards) {
            if (card.isFaceUp()) {
                numFaceUp++;
            }
        }
        if (numFaceUp > 2) {
            faceAllCardsDown();
            return;
        }

        for (Card card : gameCards) {
            if (card.isFaceUp()) {
                card.setVisible(false);
                card.flipCard();
                card.setIcon(card.getCardBack());
                card.removeMouseListener(card.getMouseListeners()[0]);
                card.setRemoved(true);
            }
        }
    }

    private void reactivateCards() {
        for (Card card : gameCards) {
            if (card.isRemoved()) {
                card.setVisible(true);
                card.setRemoved(false);
                card.setIcon(card.getCardBack());
                if (card.isFaceUp()) {
                    card.flipCard();
                }
            }
        }
        shuffle();
        assignMouseListener();
        reAddCards();
    }

    private void reAddCards() {
        for (Card card : gameCards) {
            cardPanel.remove(card);
        }
        for(int i = 0; i < gameCards.length; i++) {
            cardPanel.add(gameCards[i]);
        }
    }

    private void gameOver() {
        for (Card card : gameCards) {
            if (!card.isRemoved()) {
                card.removeMouseListener(card.getMouseListeners()[0]);
            }
        }
        scoreLabel.setText("GAME OVER");
        attemptsLabel.setText("Final Score: " + score);
    }
}