package com.example.javaableton;

import java.util.ArrayList;
import java.util.List;

public class Track {

    private String name;
    private List<Plugin> pluginList = new ArrayList<>();
    private List<AudioClip> audioClipList = new ArrayList<>();
    private Boolean isFrozen;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Plugin> getPluginList() {
        return pluginList;
    }

    public void setPluginList(List<Plugin> pluginList) {
        this.pluginList = pluginList;
    }

    public List<AudioClip> getAudioClipList() {
        return audioClipList;
    }

    public void setAudioClipList(List<AudioClip> audioClipList) {
        this.audioClipList = audioClipList;
    }

    public Boolean getFrozen() {
        return isFrozen;
    }

    public void setFrozen(Boolean frozen) {
        isFrozen = frozen;
    }
}
