package com.bobona.apri.visual.animation;

// this class serves as a way to quickly convert a test_animation to an object using gson
public class GsonEntityAnimation {

    public String name;
    public float width;
    public float height;
    public Layer[] layers;

    public GsonEntityAnimation() {

    }

    public class Layer {

        public String name;
        Shape[] shapes;
        Frame[] frames;

        private Layer() {}
    }

    public class Shape {

        String name;
        String type;
        float width, height;
        float x, y;
        float rotation;
        float zIndex;
        Transform[] referencePoints;

        private Shape() {}
    }

    public class Frame {

        int duration;
        Transform[] transforms;

        private Frame() {}
    }

    public class Transform {

        public float zIndex;
        public String name;
        public float x, y;
        public float scaleX, scaleY;
        public float rot;
        public boolean origin;
        public float rotX, rotY;
        public boolean hide;

        private Transform() {}
    }
}
