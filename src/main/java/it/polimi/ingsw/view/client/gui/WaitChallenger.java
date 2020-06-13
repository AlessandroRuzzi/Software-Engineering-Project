package it.polimi.ingsw.view.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static it.polimi.ingsw.view.client.gui.BackgroundButton.backgroundButtonPersonalized;
import static it.polimi.ingsw.view.client.gui.Gui.felixBold;
import static it.polimi.ingsw.view.client.gui.Gui.getD;

/**
 * Classe che estende JDesktopPane che costruisce pane per il messaggio di attesa delle scelte del challenger
 * @author Scilux
 * @version 1.0
 * @since 2020/06/13
 */

public class WaitChallenger extends JDesktopPane{

    Dimension frameSize = new Dimension();
    MyButton close = new MyButton(3);
    JInternalFrame intFrame;
    String nameChoosing;
    double posx;

    /**
     * Costruttore della classe
     * @param frame Riferimento al JInternalFrame in cui verrà inserito l'attuale JDesktopPane WaitChallenger
     * @param dim Dimensione del JInternalFrame
     * @param name Nome del Player designato come Challenger
     * @throws IOException se il caricamento delle scritte non andasse a buon fine
     */

    public WaitChallenger(JInternalFrame frame, Dimension dim, String name) throws IOException {

        frameSize.setSize(dim);
        intFrame = frame;
        setPreferredSize(frameSize);
        setLayout(null);
        nameChoosing = name;
        posx = (double) nameChoosing.length() / 2;

        JLabel label = ImageHandler.setImage("resources/Graphics/Texts/wait_for.png", 99, 99, frameSize.width * 25/100, frameSize.height * 20/100);
        JLabel label2 = ImageHandler.setImage("resources/Graphics/Texts/as_challenger_to_choose_the_gods.png", 99, 99, frameSize.width * 85/100, frameSize.height * 22/100);
        JLabel label3 = ImageHandler.setImage("resources/Graphics/Texts/and_the_first_player.png", 99, 99, frameSize.width * 60/100, frameSize.height * 22/100);
        label.setBounds((int) (frameSize.width * 18.5/100), (int) (frameSize.height * 25.5/100), frameSize.width * 25/100, frameSize.height * 20/100);
        label2.setBounds((int) (frameSize.width * 7.5/100), (int) (frameSize.height * 39.5/100), frameSize.width * 85/100, frameSize.height * 22/100);
        label3.setBounds((int) (frameSize.width * 20/100), (int) (frameSize.height * 54.5/100), frameSize.width * 60/100, frameSize.height * 22/100);

        JLabel otherName = new JLabel(nameChoosing);
        otherName.setBounds((int) (frameSize.width * 45.75/100), (int) (frameSize.height * 32.5/100), frameSize.width * 50/100, frameSize.width * 5/100);
        otherName.setFont(felixBold);

        add(label);
        add(label2);
        add(label3);
        add(otherName);


        close.addActionListener(new Close());
        close.setBounds((int) (((double)frameSize.width * 50/ 100) - ((getD().getWidth() * 13 / 100) * 50/100)), (int) (frameSize.height * 81 / 100), (int) (getD().getWidth() * 13 / 100), (int) (getD().getHeight() * 5 / 100));
        add(close);

        JButton back = backgroundButtonPersonalized(2, frameSize);
        back.setBounds(0, 0, frameSize.width, frameSize.height);
        add(back);
    }

    /**
     * Classe che implementa ActionListener per il JButton Close che chiude l'attuale JInternalFrame
     */

    private class Close implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            intFrame.setVisible(false);
        }
    }
}
