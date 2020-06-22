package com.bobona.animationMaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecentAnimations extends ArrayList<RecentAnimation> {

    public RecentAnimations() {
        super();
        load();
    }

    public void load() {
        try {
            List<String> recentAnimationsData = Arrays.asList(Gdx.files.local("recentlyOpened.txt").readString().split("\r"));
            List<RecentAnimation> recentAnimations = new ArrayList<>();
            for (int i = 0; i < recentAnimationsData.size() - 1; i += 2) {
                recentAnimations.add(new RecentAnimation(
                        recentAnimationsData.get(i),
                        recentAnimationsData.get(i + 1)));
            }
            Collections.reverse(recentAnimations);
            clear();
            addAll(recentAnimations);
        } catch (GdxRuntimeException ioe) {
            ioe.printStackTrace();
            Gdx.files.local("recentlyOpened.txt").write(false);
            clear();
        }
    }

    @Override
    public boolean add(RecentAnimation recentAnimation) {
        boolean returnValue = super.add(recentAnimation);
        updateFile();
        return returnValue;
    }

    @Override
    public RecentAnimation remove(int index) {
        RecentAnimation returnValue = super.remove(index);
        updateFile();
        return returnValue;
    }

    @Override
    public boolean remove(Object o) {
        boolean returnValue = super.remove(o);
        updateFile();
        return returnValue;
    }

    @Override
    public RecentAnimation set(int index, RecentAnimation element) {
        RecentAnimation returnValue = super.set(index, element);
        updateFile();
        return returnValue;
    }

    private void updateFile() {
        String output = "";
        for (RecentAnimation recentAnimation : this) {
            output += recentAnimation.name + "\r";
            output += recentAnimation.path + "\r";
        }
        Gdx.files.local("recentlyOpened.txt").writeString(output, false);
    }
}
