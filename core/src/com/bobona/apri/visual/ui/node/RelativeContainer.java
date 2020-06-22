package com.bobona.apri.visual.ui.node;

import com.bobona.apri.entity.Entity;
import com.bobona.apri.visual.Renderable;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class RelativeContainer extends UiNode {

    public RelativeContainer(String name, Node node) {
        super(name, node);
    }

    @Override
    public List<Renderable> getRenderables(Entity entity, float width, float height) {
        List<Renderable> renderables = new ArrayList<>();
        for (UiNode node : children) {
            List<Renderable> nodeRenderables = node.getRenderablesRelative(entity, width, height);
            Renderable.transformRenderables(nodeRenderables, node.x.getValue(width), node.y.getValue(height));
            for (Renderable renderable : nodeRenderables) {
                renderable.getTransform().xOffset += width * alignment.getTransformX();
                renderable.getTransform().yOffset += height * alignment.getTransformY();
            }
            renderables.addAll(nodeRenderables);
        }
        return renderables;
    }
}
