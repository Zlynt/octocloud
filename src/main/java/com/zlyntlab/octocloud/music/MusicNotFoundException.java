package com.zlyntlab.octocloud.music;

public class MusicNotFoundException extends RuntimeException {

    public MusicNotFoundException(){
        super("Could not find the music");
    }
}
