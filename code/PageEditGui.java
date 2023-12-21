import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.*;

public class PageEditGui implements ActionListener, WindowListener {
    JFrame gui;
    Background panel;
    JTextField[] inputs;
    JButton toggleFM, Save, toggleSM;
    JLabel anilabel;
    int index;
    String[] labels = { "Last Name", "Address", "Zipcode", "Homephone", "Name", "Work", "Work Number", "Cell Number",
            "Email", "Birthday", "Anniversary", "Children" };
        
    private static String[] VisualData = {"4!ForeGround.png!175,250,350,500,0"};
    String oldData, NewData, temp;
    boolean displayToggle = true, single = false;
    String[] sleeperData = new String[6];
    MainGUI g;

    PageEditGui(int index, MainGUI t) { // pass in a normal number for editing a page, pass in a -1 to add a new page

        g = t; // dont ask ._.

        if(Database.databaseLength == 0 && index != -1)
            System.exit(1);

        if (index != -1)
            gui = new JFrame("Editing the " + Database.getEntry(index).getKey() + "'s page");
        else
            gui = new JFrame("Creating New Page");
        
        gui.setSize(350, 500);
        gui.setResizable(false);
        gui.addWindowListener(this);
        panel = new Background(VisualData);
        panel.setLayout(null);
        this.index = index;
        gui.add(panel);

        if (index == -1) {
            Database.addEntry();
            this.index = Database.getLastIndex();
            MainGUI.setindex(Database.getLastIndex());
        }

        oldData = Database.getEntry(this.index).gitInfo();
        inputs = new JTextField[12];
        switch (Database.getEntry(this.index).getFamilyType()) {
            case 2:
            case 3:
                single = true;
                break;

            case 4:
            case 5:
                single = false;
                break;

        }

        // adding the single/married toggle
        toggleSM = new JButton(single ? "Single" : "Married");
        toggleSM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleSM.setBounds(5, 5, 150, 25);
        toggleSM.addActionListener(this);
        panel.add(toggleSM);

        // start of adding the text fields and toggle button

        for (int i = 0; i <= 3; i++) {
            inputs[i] = new JTextField(Database.getEntry(this.index).getHeadingData()[i], 5);
            inputs[i].setBounds(100, 35 + (i * 30), 150, 25);
            JLabel label = new JLabel(labels[i]);
            label.setForeground(new Color(0x00A8F3));
            label.setBounds(15, 35 + (i * 30), 150, 25);

            panel.add(inputs[i]);
            panel.add(label);
        }

        toggleFM = new JButton("Father");
        toggleFM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleFM.setBounds(5, 150, 150, 25);
        toggleFM.addActionListener(this);
        toggleFM.setEnabled(!single);
        panel.add(toggleFM);

        for (int i = 0; i < sleeperData.length; i++) {
            sleeperData[i] = Database.getEntry(this.index).getFatherData()[i];
        }

        for (int i = 4; i <= 9; i++) {
            inputs[i] = new JTextField(sleeperData[i - 4], 1);
            inputs[i].setBounds(110, 60 + (i * 30), 150, 25);
            JLabel label = new JLabel(labels[i]);
            label.setForeground(new Color(0x00A8F3));
            label.setBounds(15, 60 + (i * 30), 150, 25);

            panel.add(label);
            panel.add(inputs[i]);
        }

        for (int i = 0; i < sleeperData.length; i++) {
            sleeperData[i] = Database.getEntry(this.index).getMotherData()[i];
        }

        int i = 10;

        System.out.println("(PageEditGui.init) adding ani date" + displayToggle);
        anilabel = new JLabel(labels[i]);
        anilabel.setForeground(new Color(0x00A8F3));
        anilabel.setBounds(15, 65 + (i * 30), 150, 25);
        inputs[i] = new JTextField(Database.getEntry(this.index).getAnniversary(), 1);
        inputs[i].setBounds(100, 65 + (i * 30), 150, 25);

        inputs[10].setEnabled(!single);
        anilabel.setEnabled(!single);

        panel.add(anilabel);
        panel.add(inputs[i]);
        i++;

        JLabel label = new JLabel(labels[i]);
        label.setForeground(new Color(0x00A8F3));
        label.setBounds(15, 65 + (i * 30), 150, 25);
        inputs[i] = new JTextField(Toolbox.arrtostr(Database.getEntry(this.index).getChildren()), 1);
        inputs[i].setBounds(100, 65 + (i * 30), 150, 25);

        panel.add(label);
        panel.add(inputs[i]);

        for(index = 0 ; index < inputs.length; index++) {
            inputs[i].setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        }

        gui.add(panel);
        gui.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent act) {
        if (act.getSource() == toggleFM) {
            swapCouplesData();
            toggleFM.setText(displayToggle ? "Father" : "Mother");
           
            System.out.println("(PageEditGui.actionPerformed) Swaping Couples Data, " + displayToggle);
        } else if (act.getSource() == toggleSM) {
            single = !single;
            toggleSM.setText(single ? "Single" : "Married");
            toggleFM.setEnabled(!single);
            inputs[10].setEnabled(!single);
            anilabel.setEnabled(!single);
        }
    }

    private void swapCouplesData() {
        // this function will affect the textfields 4 thru 9 and swap them with the
        // sleeper memory
        for (int i = 4; i <= 9; i++) {
            temp = inputs[i].getText();
            inputs[i].setText(sleeperData[i - 4]);
            sleeperData[i - 4] = temp;
        }
        displayToggle = !displayToggle;
        System.out.println("(PageEditGui.swapCouplesData) sleeperData -> " + Arrays.toString(sleeperData)
                + " : ToggleFM -> " + displayToggle);
    }

    // TODO : make it to where when the window is closed all the changes are saved
    // and sent to the database

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("(PageEditGui.windowClosing) Window Closing, Saving Data");

        System.out.println("(PageEditGui.windowClosing) Old Data -> " + Database.getEntry(this.index).gitInfo());
        saveToDatabase();
        System.out.println("(PageEditGui.windowClosing) New Data -> " + Database.getEntry(this.index).gitInfo());

        gui.dispose();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("(PageEditGui.windowOpened) windowOpened");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("(PageEditGui.windowClosed) WindowClosed");
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowClosed'");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        System.out.println("(PageEditGui.windowIconified) Windowconified");
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowIconified'");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        System.out.println("(PageEditGui.windowDeiconified) windowsdeiconified");
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowDeiconified'");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        System.out.println("(PageEditGui.windowActivated) windowsActivated");
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowActivated'");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        System.out.println("(PageEditGui.windowDeactivated) windowDeactivated");
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowDeactivated'");
    }

    private void saveToDatabase() {
        int newFamilyType = 0;
        String[] temp = new String[4];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = inputs[i].getText().trim();
        }
        Database.getEntry(this.index).writeHeadingData(temp);

        for (int x = 0; x < 2; x++) { // this loop gets the couples data
            temp = new String[6];
            if (single) {
                swapCouplesData();
                x++;
            }
            for (int i = 0; i < 6; i++) {
                temp[i] = sleeperData[i];
            }

            if (displayToggle) { // true = mother, false = father, this is what data is in sleeper data
                Database.getEntry(this.index).writeMotherData(temp);
            } else {
                Database.getEntry(this.index).writeFatherData(temp);
            }
            swapCouplesData();

        }

        Database.getEntry(this.index)
                .writeChildren(inputs[11].getText().trim().split(Character.toString(Database.childrenDelimiter)));
        Database.getEntry(this.index).writeAnniversaryDate(inputs[10].getText());

        if (single && inputs[inputs.length - 1].getText().length() == 0) {
            newFamilyType = 2;
        } else if (single && inputs[inputs.length - 1].getText().length() != 0) {
            newFamilyType = 3;
        } else if (!single && inputs[inputs.length - 1].getText().length() == 0) {
            newFamilyType = 4;
        } else if (!single && inputs[inputs.length - 1].getText().length() != 0) {
            newFamilyType = 5;
        }
        Database.getEntry(this.index).writeFamType(newFamilyType);

        try {
            g.updatePage();
        } catch (IOException err) {
            System.out.println("(PageEditGui.saveToDatabase) Error with updating page!");
        }
    }
}
