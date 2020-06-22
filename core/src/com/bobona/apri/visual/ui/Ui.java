package com.bobona.apri.visual.ui;

import com.bobona.apri.entity.Entity;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.ui.node.UiNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;

public class Ui {

    String name; // By convention, entities should only give nodes data through properties under the name path
    UiNode rootNode;

    public Ui(String name, Document document) {
        this.name = name;
        Node root = document.getDocumentElement();
        rootNode = UiNode.createNode(name, root.getFirstChild().getNextSibling());
    }

    public List<Renderable> getRenderables(Entity hostEntity, float width, float height) {
        return rootNode.getRenderables(hostEntity, width, height);
    }

    public String getName() {
        return name;
    }


}
