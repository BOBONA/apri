package com.bobona.animationMaker;

import java.io.File;
import java.util.Objects;

public class RecentAnimation {

    public String name;
    public String path;

    public RecentAnimation(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path);
    }
}
