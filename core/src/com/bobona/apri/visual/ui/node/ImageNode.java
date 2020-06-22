package com.bobona.apri.visual.ui.node;

import com.bobona.apri.ApriWorld;
import com.bobona.apri.entity.Entity;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.Transform;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class ImageNode extends UiNode {

    String path;

    public ImageNode(String name, Node node) {
        super(name, node);
        path = getAttribute("path");
        ApriWorld.addTexture(path);
    }

    @Override
    public List<Renderable> getRenderables(Entity entity, float width, float height) {
        List<Renderable> renderables = new ArrayList<>();
        renderables.add(new Renderable(new Transform(0, 0, width, height), path));
        return renderables;
    }
}
