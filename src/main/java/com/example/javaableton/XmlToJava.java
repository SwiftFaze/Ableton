package com.example.javaableton;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import classes.Ableton;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XmlToJava {

    public String filePath = "";
    private Document document;

//    public static Ableton convert() throws IOException, JSONException {
//
//        String fileName = "C:\\Users\\Rob\\IdeaProjects\\Ableton_stats\\resource\\ableton_projects\\test.xml";
//        File xmlFile = new File(fileName);
//        byte[] b = Files.readAllBytes(xmlFile.toPath());
//        String xml = new String(b);
//        JSONObject json = XML.toJSONObject(xml);
//        String jsonString = json.get("Ableton").toString();
//        return new Gson().fromJson(jsonString, Ableton.class);
//
//    }

    XmlToJava(String path) {
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

    XmlToJava() {
        this.filePath = null;
        this.document = null;
    }


//    public void xmlConvert() {
//
//        //Get the Document Builder
//
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        try {
//            DocumentBuilder builder = factory.newDocumentBuilder();
//
//            // Get Document
//            Document document = builder.parse(new File(this.filePath));
//
//            // Normalize the xml structure
//            document.getDocumentElement().normalize();
//
//            // Get all the element by the tag name
//
////            printProjectVersion(document);
//            System.out.println("Information");
//            System.out.println("• Version : " + getProjectVersion(document));
//            System.out.println("• BPM : " + getInfomation(document, "Tempo", "Manual"));
//            System.out.println("");
//
//            System.out.println("Third Party Plugins");
//            System.out.println("- VST 2");
//            List<String> vst2PluginList = getInfomationList(document, "VstPluginInfo", "PlugName");
//            for (String vst2 : vst2PluginList) {
//                System.out.println("    • " + vst2);
//            }
//
//            System.out.println("");
//
//            System.out.println("- VST 3");
//            List<String> vst3PluginList = getInfomationList(document, "Vst3PluginInfo", "Name");
//            for (String vst3 : vst3PluginList) {
//                System.out.println("    • " + vst3);
//            }
//            System.out.println("");
//
//        } catch (ParserConfigurationException | IOException | SAXException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public String getInfomation(String tagName, String nodeName) {
        NodeList laptopList = this.document.getElementsByTagName(tagName);
        Node laptop = laptopList.item(0);
        if (laptop.getNodeType() == Node.ELEMENT_NODE) {

            Element laptopElement = (Element) laptop;
            Node elementFirstChild = laptopElement.getFirstChild();

            while (!Objects.equals(elementFirstChild.getNodeName(), nodeName)) {
                elementFirstChild = elementFirstChild.getNextSibling();
            }

            NamedNodeMap attributes = elementFirstChild.getAttributes();
            return attributes.item(0).getNodeValue();
        }
        return "N/A";
    }

    public String getProjectVersion() {
        NodeList laptopList = this.document.getElementsByTagName("Ableton");
        for (int i = 0; i < laptopList.getLength(); i++) {
            Node laptop = laptopList.item(i);
            if (laptop.getNodeType() == Node.ELEMENT_NODE) {
                Element laptopElement = (Element) laptop;
                Attr creator = laptopElement.getAttributeNode("Creator");
                return creator.getValue();

            }
        }
        return "N/A";
    }

    public List<String> getInfomationList(String tagName, String nodeName) {
        List<String> informationList = new ArrayList<>();
        NodeList laptopList = this.document.getElementsByTagName(tagName);
        for (int i = 0; i < laptopList.getLength(); i++) {
            Node laptop = laptopList.item(i);
            if (laptop.getNodeType() == Node.ELEMENT_NODE) {

                Element laptopElement = (Element) laptop;
                Node elementFirstChild = laptopElement.getFirstChild();

                while (!Objects.equals(elementFirstChild.getNodeName(), nodeName)) {
                    elementFirstChild = elementFirstChild.getNextSibling();
                }

                NamedNodeMap attributes = elementFirstChild.getAttributes();
                if (!informationList.contains(attributes.item(0).getNodeValue())) {
                    informationList.add(attributes.item(0).getNodeValue());
                }

            }
        }
        return informationList;
    }




}
