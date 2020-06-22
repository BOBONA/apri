package com.bobona.apri.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class WorldSchematic {

    public Array<IndexEntity> indexWorld;

    class SchematicPart {

        int startX;
        int startY;
        int offsetX;
        int offsetY;
        int cloneX;
        int cloneY;
        int[] tiles;

        public SchematicPart() {}
    }

    public class IndexEntity {

        public final float x;
        public final float y;
        public final int index;

        public IndexEntity(float x, float y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }
    }

    SchematicPart[] schematicParts;

    private WorldSchematic() {

    }

    public void loadIndexWorld() {
        // this looks complicated so you shouldn't touch it
        // appreciate that there are three nested for loops
        indexWorld = new Array<>();
        for (SchematicPart part : schematicParts) {
            for (int y = 0; y < part.cloneY + 1; y++) {
                for (int x = 0; x < part.cloneX + 1; x++) {
                    Vector2 pointer = new Vector2(
                            part.startX + x * (part.tiles.length * part.offsetX
                                    + (part.offsetX == 0 ? 1 : 0)),
                            part.startY + y * (part.tiles.length * part.offsetY
                                    + (part.offsetY == 0 ? 1 : 0)));
                    for (int tileIndex : part.tiles) {
                        indexWorld.add(new IndexEntity(pointer.x, pointer.y, tileIndex));
                        pointer.x += part.offsetX;
                        pointer.y += part.offsetY;
                    }
                }
            }
        }
    }
}
