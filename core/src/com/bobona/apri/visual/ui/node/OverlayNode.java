package com.bobona.apri.visual.ui.node;

import com.bobona.apri.entity.Entity;
import com.bobona.apri.visual.Renderable;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class OverlayNode extends UiNode {

    public OverlayNode(String name, Node node) {
        super(name, node);
    }

    @Override
    public List<Renderable> getRenderables(Entity hostEntity, float width, float height) {
        List<Renderable> renderables = new ArrayList<>();
        for (UiNode node : children) {
            renderables.addAll(node.getRenderables(hostEntity, width, height));
        }
        return renderables;
    }
}
