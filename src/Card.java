import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card extends JLabel{

    private final Icon cardBack;
    private final Icon cardImage;
    private boolean isFaceUp = false;
    private boolean isRemoved = false;

    public Card(String cardBack, String cardImage) throws IOException {
        this.cardBack = createCardIcon(cardBack);
        this.cardImage = createCardIcon(cardImage);
    }

    private Card(Icon cardBack, Icon cardImage) {
        this.cardBack = cardBack;
        this.cardImage = cardImage;
    }

    public Icon getCardBack() {
        return cardBack;
    }

    public Icon getCardImage() {
        return cardImage;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void flipCard() {
        if (isFaceUp) {
            isFaceUp = false;
        } else {
            isFaceUp = true;
        }
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public Card makeCopy() {
        return new Card(this.cardBack, this.cardImage);
    }

    private Icon createCardIcon(String path) throws IOException {
        BufferedImage bi = ImageIO.read(new File(path));
        Image image = bi.getScaledInstance(150, 242, Image.SCALE_SMOOTH);
        Icon icon = new ImageIcon(image);
        return icon;
    }

}
