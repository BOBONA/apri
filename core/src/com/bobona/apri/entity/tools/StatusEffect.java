package com.bobona.apri.entity.tools;

import java.util.Objects;

public class StatusEffect {

    public Type type;
    public int level;
    public int frameDuration;
    public long frameStarted;

    public StatusEffect(Type type, int level, int frameDuration, long frameStarted) {
        this.type = type;
        this.level = level;
        this.frameDuration = frameDuration;
        this.frameStarted = frameStarted;
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, level, frameDuration, frameStarted);
    }

    public enum Type {

        BURN,
        FIRE;

        public Type[] mutuallyExclusiveStatuses;

        // nasty fix to forward referencing problems
        static {
            BURN.mutuallyExclusiveStatuses = new Type[]{Type.FIRE};
            FIRE.mutuallyExclusiveStatuses = new Type[]{Type.BURN};
        }
    }
}
