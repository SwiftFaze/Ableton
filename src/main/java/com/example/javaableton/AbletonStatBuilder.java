package com.example.javaableton;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AbletonStatBuilder {

    public String filePath = "";
    private Document document;
    Project project = new Project();

    AbletonStatBuilder(String path) {
        this.buildDocument(path);
    }

    AbletonStatBuilder() {
        this.filePath = null;
        this.document = null;
    }


    private void buildDocument(String path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Get Document
            this.document = builder.parse(new File(path));
            // Normalize the xml structure
            this.document.getDocumentElement().normalize();
            this.filePath = path;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public Project getProjectStats(String path) {
        this.buildDocument(path);
        this.project.setVersion(this.getInformation("Ableton", "Creator"));
        this.project.setBpm(this.getChildInfomation("Tempo", "Manual"));
        this.project.setTrackList(this.getTrackList());
        return this.project;
    }

    public List<Track> getTrackList() {
        List<Track> trackList = new ArrayList<>();
        NodeList audioTrackNodeList = this.document.getElementsByTagName("AudioTrack");
        NodeList midiTrackNodeList = this.document.getElementsByTagName("MidiTrack");
        NodeList returnTrackNodeList = this.document.getElementsByTagName("ReturnTrack");
        NodeList masterTrackNodeList = this.document.getElementsByTagName("MasterTrack");

        trackList.addAll(getAudioTrackList(audioTrackNodeList));
        trackList.addAll(getAudioTrackList(midiTrackNodeList));
//        trackList.addAll(getAudioTrackList(returnTrackNodeList));
//        trackList.addAll(getAudioTrackList(masterTrackNodeList));


        return trackList;

    }

    private List<Track> getAudioTrackList(NodeList audioTrackNodeList) {
        List<Track> trackList = new ArrayList<>();
        for (int i = 0; i < audioTrackNodeList.getLength(); i++) {
            Node node = audioTrackNodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Track audioTrack = new Track();
                audioTrack.setName(this.getNodeChildInformation(node, "Name", "EffectiveName"));
                audioTrack.setFrozen(Boolean.valueOf(this.getNodeInformation(node, "Freeze")));
                audioTrack.setPluginList(this.getPluginList(node, audioTrack));
                trackList.add(audioTrack);
            }
        }
        return trackList;
    }

    private List<Plugin> getPluginList(Node trackNode, Track audioTrack) {
        List<Plugin> pluginList = new ArrayList<>();
        if (trackNode.getNodeType() == Node.ELEMENT_NODE) {

            Element element = (Element) trackNode;
            Node nodeChild = element.getFirstChild();

            while (!Objects.equals(nodeChild.getNodeName(), "DeviceChain")) {
                nodeChild = nodeChild.getNextSibling();
            }
            nodeChild = getNodeChild(nodeChild, "DeviceChain");
            nodeChild = getNodeChild(nodeChild, "Devices");

            if (nodeChild.getFirstChild() != null) {
                nodeChild = nodeChild.getFirstChild();
                while (nodeChild.getNextSibling() != null) {
                    nodeChild = nodeChild.getNextSibling();
                    if (!nodeChild.getNodeName().equals("#text")) {
                        Plugin plugin = new Plugin();
                        if (nodeChild.getNodeName().equals("PluginDevice")) {
                            Node externalPluginNode = nodeChild;
                            Node externalNodeChild = getNodeChild(externalPluginNode, "PluginDesc");
                            if (!Objects.equals(getNodeInformation(externalNodeChild, "Vst3PluginInfo"), "N/A")) {
                                plugin.setName(this.getNodeChildInformation(externalNodeChild, "Vst3PluginInfo", "Name"));
                                plugin.setExtension("VST3");
                                plugin.setFrozen(audioTrack.getFrozen());
                                plugin.setLocation(audioTrack.getName());
                            } else {
                                plugin.setName(this.getNodeChildInformation(externalNodeChild, "VstPluginInfo", "PlugName"));
                                plugin.setExtension("VST2");
                                plugin.setFrozen(audioTrack.getFrozen());
                                plugin.setLocation(audioTrack.getName());
                            }


                        } else {

                            plugin.setName(nodeChild.getNodeName());
                            plugin.setExtension("Native");
                            plugin.setFrozen(audioTrack.getFrozen());
                            plugin.setLocation(audioTrack.getName());


                        }
                        pluginList.add(plugin);
                    }


                }


            }


        }


        return pluginList;
    }

    private static Node getNodeChild(Node nodeChild, String Devices) {
        nodeChild = nodeChild.getFirstChild();
        while (!Objects.equals(nodeChild.getNodeName(), Devices)) {
            nodeChild = nodeChild.getNextSibling();
        }
        return nodeChild;
    }

    private String getNodeChildInformation(Node node, String tagName, String nodeName) {

        if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element element = (Element) node;
            Node nodeChild = element.getFirstChild();

            while (!Objects.equals(nodeChild.getNodeName(), tagName)) {
                if (nodeChild.getNextSibling() != null) {
                    nodeChild = nodeChild.getNextSibling();
                } else {
                    return "N/A";
                }
            }
            return this.getNodeInformation(nodeChild, nodeName);
        }
        return "N/A";


    }

    private String getNodeInformation(Node node, String nodeName) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element element = (Element) node;
            Node nodeChild = element.getFirstChild();

            while (!Objects.equals(nodeChild.getNodeName(), nodeName)) {
                if (nodeChild.getNextSibling() != null) {
                    nodeChild = nodeChild.getNextSibling();
                } else {
                    return "N/A";
                }

            }
            NamedNodeMap attributes = nodeChild.getAttributes();
            return attributes.item(0).getNodeValue();

        }
        return "N/A";


    }


    public String getChildInfomation(String tagName, String nodeName) {
        NodeList nodeList = this.document.getElementsByTagName(tagName);
        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element element = (Element) node;
            Node elementFirstChild = element.getFirstChild();

            while (!Objects.equals(elementFirstChild.getNodeName(), nodeName)) {
                elementFirstChild = elementFirstChild.getNextSibling();
            }

            NamedNodeMap attributes = elementFirstChild.getAttributes();
            return attributes.item(0).getNodeValue();
        }
        return "N/A";
    }

    public String getInformation(String tagName, String nodeName) {
        NodeList nodeList = this.document.getElementsByTagName(tagName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node laptop = nodeList.item(i);
            if (laptop.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) laptop;
                Attr creator = element.getAttributeNode(nodeName);
                return creator.getValue();
            }
        }
        return "N/A";
    }


    public List<Track> getFilteredPluginsByExtension() {
        List<Track> trackList = new ArrayList<>(this.project.getTrackList());
        for (Track track : trackList) {
            track.setPluginList(track.getPluginList().stream().filter(plugin -> !Objects.equals(plugin.getExtension(), "Native")).collect(Collectors.toList()));
        }
        return trackList;
    }
    public List<Track> getFilteredPluginsByFrozen() {
        List<Track> trackList = new ArrayList<>(this.project.getTrackList());
        for (Track track : trackList) {
            track.setPluginList(track.getPluginList().stream().filter(plugin -> !Objects.equals(plugin.getFrozen(), false)).collect(Collectors.toList()));
        }
        return trackList;
    }


}
