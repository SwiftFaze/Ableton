package com.example.javaableton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.List;
import java.util.Objects;


public class AbletonStats extends JFrame {
    private JPanel mainPanel;
    private JButton browseButton;
    private JTextPane projectUrlField;
    private JButton importButton;
    private JTable projectInfo;
    private JTable vst2PluginTable;
    private JTable vst3PluginTable;
    private JButton copyButton;
    private JProgressBar progressBar;
    XmlToJava xmlToJava;

    private Font labelFont = new Font("Courier", Font.BOLD, 16);
    private Font ValueFont = new Font("Courier", Font.PLAIN, 16);
    private int progress = 0;

    public AbletonStats() {
        this.xmlToJava = new XmlToJava();

        add(mainPanel);

        projectUrlField.setText("C:\\Users\\Rob\\IdeaProjects\\Ableton_stats\\resource\\ableton_projects\\MOOD3");
        this.xmlToJava.filePath = projectUrlField.getText();


//        versionLabel.setText("Version : ");
//        versionLabel.setFont(this.labelFont);
//
//        versionValue.setText(this.xmlToJava.getProjectVersion());
//        versionValue.setFont(this.ValueFont);

        projectInfo.setModel(new DefaultTableModel(null,
                new String[]{"Title", "Details"})
        );


        vst2PluginTable.setModel(new DefaultTableModel(null,
                new String[]{"VST2 Plugins"})
        );

        vst3PluginTable.setModel(new DefaultTableModel(null,
                new String[]{"VST3 Plugins"})
        );


        projectInfo.setFont(ValueFont);
        vst2PluginTable.setFont(ValueFont);
        vst3PluginTable.setFont(ValueFont);

        projectInfo.setRowHeight(30);

        vst2PluginTable.setRowHeight(30);
        vst3PluginTable.setRowHeight(30);




        setTitle("Ableton information");
        setSize(700, 1000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == browseButton) {
                    JFileChooser fileChooser = new JFileChooser();
                    int response = fileChooser.showOpenDialog(null);
                    if (response == JFileChooser.APPROVE_OPTION) {
                        xmlToJava = new XmlToJava(fileChooser.getSelectedFile().getAbsolutePath());
                        projectUrlField.setText(xmlToJava.filePath);
                    }
                }
            }


        });

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == importButton) {
                    createStats();

                }


            }


        });

        projectUrlField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyCode() == 10) {
                    projectUrlField.setText(xmlToJava.filePath);
                    copyButton.grabFocus();
                    createStats();
                } else {
                    xmlToJava.filePath = projectUrlField.getText();

                }
            }
        });
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection("helloo");
                clipboard.setContents(stringSelection, stringSelection);
                JOptionPane.showMessageDialog(null, "Copied");
            }
        });

    }


    public static void main(String[] args) {
        AbletonStats myAbletonStats = new AbletonStats();

    }

    private void createStats() {
        progressBar.setValue(0);
        if (!Objects.equals(xmlToJava.filePath, null)) {
//                        C:\Users\Rob\IdeaProjects\Ableton_stats\resource\ableton_projects\MOOD3
            xmlToJava = new XmlToJava(xmlToJava.filePath);
            Object[][] data = {{"Version", xmlToJava.getProjectVersion()},
                    {"BPM", xmlToJava.getInfomation("Tempo", "Manual")}};

            projectInfo.setModel(new DefaultTableModel(
                    data,
                    new String[]{"Title", "Details"})
            );
            progressBar.setValue(33);
            List<String> infomationList = xmlToJava.getInfomationList("VstPluginInfo", "PlugName");
            String[][] arr = new String[infomationList.size()][1];
            for (int i = 0; i < infomationList.size(); i++) {
                arr[i][0] = infomationList.get(i);
            }
            vst2PluginTable.setModel(new DefaultTableModel(
                    arr,
                    new String[]{"VST2 Plugins"})
            );
            progressBar.setValue(66);
            List<String> infomationList2 = xmlToJava.getInfomationList("Vst3PluginInfo", "Name");
            String[][] arr2 = new String[infomationList2.size()][1];
            for (int i = 0; i < infomationList2.size(); i++) {
                arr2[i][0] = infomationList2.get(i);
            }
            vst3PluginTable.setModel(new DefaultTableModel(
                    arr2,
                    new String[]{"VST3 Plugins"})
            );
            progressBar.setValue(100);

        }
    }
}
