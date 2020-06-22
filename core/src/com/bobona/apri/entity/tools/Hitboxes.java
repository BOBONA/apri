package com.bobona.apri.entity.tools;

public class Hitboxes {

    public static float[] RECTANGLE = new float[]{0, 0, 0, 1, 1, 1, 1, 0};
    public static float[] RECTANGLE_CLIPPED = new float[]{0.35f, 0, 0, 0.05f, 0, 1, 1, 1, 1, 0.05f, 0.65f, 0};
    public static float[] RECTANGE_CLIPPED_BOTTOM = new float[]{0.05f, 0, 0.05f, 0.05f, 1-0.05f, 0.05f, 1-0.05f, 0};
    public static float[] RECTANGE_CLIPPED_SIDE_LEFT = new float[]{0, 0.05f, 0, 1, 0.05f, 1, 0.05f, 0.05f};
    public static float[] RECTANGE_CLIPPED_SIDE_RIGHT = new float[]{1-0.05f, 0.05f, 1-0.05f, 1, 1, 1, 1, 0.05f};
}