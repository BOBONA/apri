package com.bobona.apri.visual.ui.node;

import com.bobona.apri.entity.Entity;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.ui.RelativeValue;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public abstract class UiNode {

    Node node;
    String name;
    List<UiNode> children;
    String id;
    RelativeValue x, y;
    RelativeValue width, height;
    Renderable.Align alignment;

    public UiNode(String uiName, Node node) {
        this.node = node;
        name = uiName;
        children = new ArrayList<>();
        if (node.hasChildNodes()) {
            Node child = node.getFirstChild().getNextSibling();
            while (child != null) {
                children.add(createNode(uiName, child));
                child = child.getNextSibling().getNextSibling();
            }
        }
        id = getAttribute("id");
        x = new RelativeValue(getAttribute("x"));
        y = new RelativeValue(getAttribute("y"));
        width = new RelativeValue(getAttribute("width"));
        height = new RelativeValue(getAttribute("height"));
        alignment = Renderable.getAlign(getAttribute("alignment"));
    }

    public String getAttribute(String name) {
        Node attributeNode = node.getAttributes().getNamedItem(name);
        return attributeNode == null ? "" : attributeNode.getTextContent();
    }

    public static String getAttribute(Node node, String name) {
        Node attributeNode = node.getAttributes().getNamedItem(name);
        return attributeNode == null ? "" : attributeNode.getTextContent();
    }

    public static UiNode createNode(String name, Node node) {
        String tagName = node.getNodeName();
        switch (tagName) {
            case TagNames.RELATIVE_CONTAINER:
                return new RelativeContainer(name, node);
            case TagNames.OVERLAY:
                return new OverlayNode(name, node);
            case TagNames.LINEAR_DISPLAY:
                return new LinearDisplayNode(name, node);
            case TagNames.TEXT:
                return new TextNode(name, node);
            case TagNames.IMAGE:
                return new ImageNode(name, node);
        }
        throw new RuntimeException("Invalid tag");
    }

    public abstract List<Renderable> getRenderables(Entity hostEntity, float width, float height);

    public List<Renderable> getRenderablesRelative(Entity hostEntity, float totWidth, float totHeight) {
        return getRenderables(hostEntity, width.getValue(totWidth), height.getValue(totHeight));
    }

    private static class TagNames {

        static final String RELATIVE_CONTAINER = "RelativeContainer";
        static final String OVERLAY = "Overlay";
        static final String LINEAR_DISPLAY = "LinearDisplay";
        static final String TEXT = "TextNode";
        static final String IMAGE = "Image";
    }
}
