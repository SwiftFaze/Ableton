package com.example.javaableton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.List;


public class AbletonStats extends JFrame {


    private Project project;
    private JPanel mainPanel;
    private JButton browseButton;
    private JTextPane projectUrlField;
    private JButton importButton;
    private JTable projectInfo;
    private JTable vst2PluginTable;
    private JTable vst3PluginTable;
    private JButton copyButton;
    private JProgressBar progressBar;
    private JCheckBox filterPluginTypeButton;
    private JCheckBox filterFrozenCheckBox;
    private AbletonStatBuilder abletonStatBuilder;

    private Font labelFont = new Font("Courier", Font.BOLD, 16);
    private Font ValueFont = new Font("Courier", Font.PLAIN, 16);
    private int progress = 0;

    public AbletonStats() {
        this.abletonStatBuilder = new AbletonStatBuilder();

        add(mainPanel);

        projectUrlField.setText("C:\\Users\\Rob\\IdeaProjects\\Ableton_stats\\resource\\ableton_projects\\test.xml");
        this.abletonStatBuilder.filePath = projectUrlField.getText();


//        versionLabel.setText("Version : ");
//        versionLabel.setFont(this.labelFont);
//
//        versionValue.setText(this.xmlToJava.getProjectVersion());
//        versionValue.setFont(this.ValueFont);

        projectInfo.setModel(new DefaultTableModel(null,
                new String[]{"Field", "Details"})
        );


        vst2PluginTable.setModel(new DefaultTableModel(null,
                new String[]{"Name", "Type", "Track name", "Frozen"})
        );

        vst3PluginTable.setModel(new DefaultTableModel(null,
                new String[]{"Audio clips"})
        );


        projectInfo.setFont(ValueFont);
        vst2PluginTable.setFont(ValueFont);
        vst3PluginTable.setFont(ValueFont);

        projectInfo.setRowHeight(30);

        vst2PluginTable.setRowHeight(30);
        vst3PluginTable.setRowHeight(30);


        setTitle("Ableton information");
        setSize(1000, 1000);
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
                        abletonStatBuilder = new AbletonStatBuilder(fileChooser.getSelectedFile().getAbsolutePath());
                        projectUrlField.setText(abletonStatBuilder.filePath);
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
                    projectUrlField.setText(abletonStatBuilder.filePath);
                    copyButton.grabFocus();
                    createStats();
                } else {
                    abletonStatBuilder.filePath = projectUrlField.getText();

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


        filterPluginTypeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseClicked(e);
                if (project != null) {
                    if (filterPluginTypeButton.isSelected()) {

                        setPluginTable(abletonStatBuilder.getFilteredPluginsByExtension());
                    } else {
                        setPluginTable(abletonStatBuilder.getTrackList());
                    }

                }

            }
        });
        filterFrozenCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (project != null) {
                    if (filterFrozenCheckBox.isSelected()) {
                        setPluginTable(abletonStatBuilder.getTrackList());
                        setPluginTable(abletonStatBuilder.getFilteredPluginsByFrozen());
                    } else {
                        setPluginTable(abletonStatBuilder.getTrackList());
                    }

                }


            }
        });
    }


    public static void main(String[] args) {
        AbletonStats myAbletonStats = new AbletonStats();

    }

    private void createStats() {

        this.project = abletonStatBuilder.getProjectStats("C:\\Users\\Rob\\IdeaProjects\\Ableton_stats\\resource\\ableton_projects\\test.xml");


        Object[][] data = {
                {"Version", project.getVersion()},
                {"Bpm", project.getBpm()}};


        projectInfo.setModel(new DefaultTableModel(
                data,
                new String[]{"Field", "Details"})
        );


//        setPluginTable(abletonStatBuilder.getFilteredPluginsByExtension());
        setPluginTable(abletonStatBuilder.getFilteredPluginsByFrozen());


//        if (!Objects.equals(abletonStatBuilder.filePath, null)) {
//                        C:\Users\Rob\IdeaProjects\Ableton_stats\resource\ableton_projects\MOOD3


//            List<String> infomationList = abletonStatBuilder.getInfomationList("VstPluginInfo", "PlugName");
//            String[][] arr = new String[infomationList.size()][1];
//            for (int i = 0; i < infomationList.size(); i++) {
//                arr[i][0] = infomationList.get(i);
//            }
//            vst2PluginTable.setModel(new DefaultTableModel(
//                    arr,
//        new String[]{"Name", "Type", "Frozen"})
//            );
//            progressBar.setValue(66);
//            List<String> infomationList2 = abletonStatBuilder.getInfomationList("Vst3PluginInfo", "Name");
//            String[][] arr2 = new String[infomationList2.size()][1];
//            for (int i = 0; i < infomationList2.size(); i++) {
//                arr2[i][0] = infomationList2.get(i);
//            }
//            vst3PluginTable.setModel(new DefaultTableModel(
//                    arr2,
//                    new String[]{"VST3 Plugins"})
//            );
//            progressBar.setValue(100);

//        }
    }

    private void setPluginTable(List<Track> trackList) {
        int offset = 0;
        int tableSizeOffset = 0;
        for (Track track : trackList) {
            if (track.getPluginList().size() == 0) {
                tableSizeOffset++;
            }
        }
        String[][] arr = new String[trackList.size() - tableSizeOffset][4];
        for (int i = 0; i < trackList.size(); i++) {

            Track track = trackList.get(i);
            List<Plugin> pluginList = track.getPluginList();
            if (pluginList.isEmpty()) {
                offset++;
            } else {
                for (Plugin plugin : pluginList) {
                    arr[i - offset][0] = plugin.getName();
                    arr[i - offset][1] = plugin.getExtension();
                    arr[i - offset][2] = plugin.getLocation();
                    arr[i - offset][3] = plugin.getFrozen().toString();
                }
            }


        }
        vst2PluginTable.setModel(new DefaultTableModel(
                arr,
                new String[]{"Name", "Type", "Track name", "Frozen"})
        );
    }
}
