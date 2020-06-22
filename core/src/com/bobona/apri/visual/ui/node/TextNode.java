package com.bobona.apri.visual.ui.node;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.bobona.apri.entity.Entity;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.TextData;
import com.bobona.apri.visual.Transform;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class TextNode extends UiNode {

    String font;
    String displayProperty;
    Color color;

    public TextNode(String uiName, Node node) {
        super(uiName, node);
        font = getAttribute("font");
        displayProperty = getAttribute("text_property");
        color = new Color(
                1/Float.parseFloat(getAttribute("r")),
                1/Float.parseFloat(getAttribute("g")),
                1/Float.parseFloat(getAttribute("b")),
                Float.parseFloat(getAttribute("a"))
        );
    }

    @Override
    public List<Renderable> getRenderables(Entity hostEntity, float width, float height) {
        List<Renderable> renderables = new ArrayList<>();
        Renderable renderable = new Renderable((String) hostEntity.getProperty(name).getPath(displayProperty, String.class).get(), font, color, alignment, 0, 0, height, width);
        renderables.add(renderable);
        return renderables;
    }
}
