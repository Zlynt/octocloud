package com.zlyntlab.octocloud.music;

public class ArtistNotFoundException extends RuntimeException {

    public ArtistNotFoundException(){
        super("Could not find the artist");
    }
}
