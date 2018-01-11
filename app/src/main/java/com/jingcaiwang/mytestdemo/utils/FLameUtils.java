package com.jingcaiwang.mytestdemo.utils;

/**
 * Created by jiang_yan on 2017/12/12.
 */


public class FLameUtils {
    private int numChannels = 1;
    private int sampleRate = 16000;
    private int bitRate = 96;

    static {
        System.loadLibrary("mp3lame");
    }

    private native void initEncoder(int var1, int var2, int var3, int var4, int var5);

    private native void destroyEncoder();

    private native int encodeFile(String var1, String var2);

    public int getNumChannels() {
        return this.numChannels;
    }

    public void setNumChannels(int numChannels) {
        this.numChannels = numChannels;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getBitRate() {
        return this.bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public FLameUtils() {
    }

    public FLameUtils(int numChannels, int sampleRate, int bitRate) {
        this.numChannels = numChannels;
        this.sampleRate = sampleRate;
        this.bitRate = bitRate;
    }

    public boolean raw2mp3(String source, String destination) {
        this.initEncoder(this.numChannels, this.sampleRate, this.bitRate, 1, 2);
        int result = this.encodeFile(source, destination);
        this.destroyEncoder();
        return result == 0;
    }
}
