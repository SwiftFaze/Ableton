package com.example.javaableton;

import classes.Ableton;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class XmlToJava {

    public static Ableton convert() throws IOException, JSONException {

        String fileName = "C:\\Users\\Rob\\IdeaProjects\\Ableton_stats\\resource\\ableton_projects\\project1";
        File xmlFile = new File(fileName);
        byte[]b = Files.readAllBytes(xmlFile.toPath());
        String xml = new String(b);
        JSONObject json = XML.toJSONObject(xml);
        String jsonString  = json.get("Ableton").toString();
        return  new Gson().fromJson(jsonString, Ableton.class);

    }


}
