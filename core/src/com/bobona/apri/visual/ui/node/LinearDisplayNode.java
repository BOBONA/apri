package com.bobona.apri.visual.ui.node;

import com.bobona.apri.ApriWorld;
import com.bobona.apri.entity.Entity;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.Transform;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class LinearDisplayNode extends UiNode {

    String path;
    Method method;
    String percentageProperty;

    public LinearDisplayNode(String name, Node node) {
        super(name, node);
        path = getAttribute("path");
        ApriWorld.addTexture(path);
        method = Method.valueOf(getAttribute("method").toUpperCase());
        percentageProperty = getAttribute("percent_property");
    }

    @Override
    public List<Renderable> getRenderables(Entity entity, float width, float height) {
        List<Renderable> renderables = new ArrayList<>();
        switch (method) {
            case STRETCH:
                renderables.add(new Renderable(new Transform(0, 0,
                                width*entity.getProperty(name).getPath(percentageProperty, Float.class).get(), height), path));
        }
        return renderables;
    }

    enum Method {
        STRETCH
    }
}
