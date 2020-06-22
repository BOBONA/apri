package com.bobona.animationMaker.controllers;

import com.bobona.animationMaker.Application;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.Transform;
import com.bobona.apri.visual.animation.EntityAnimation;
import com.bobona.apri.visual.animation.Layer;
import com.bobona.apri.visual.animation.Shape;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EditorController implements Controller{

    Application application;
    EntityAnimation entityAnimation;
    Layer currentLayer;
    int selectedFrame;
    String selectedShape;
    String lastShape;
    AnimationTimer timer;

    @FXML SplitPane root;
    @FXML AnchorPane canvasPane;
    @FXML Canvas canvas;
    @FXML VBox shapeData;
    @FXML Button playFrames;
    @FXML ListView<HBox> frames;
    @FXML VBox polygons;
    @FXML TextField name;
    @FXML ChoiceBox<String> shape;
    @FXML TextField width;
    @FXML TextField height;
    @FXML TextField x;
    @FXML TextField y;
    @FXML TextField rotation;

    public void initialize(Application application, EntityAnimation entityAnimation) {
        this.application = application;
        this.entityAnimation = entityAnimation;
        currentLayer = entityAnimation.getPrimaryLayer();
        selectedFrame = 0;
        selectedShape = "";
        name.setOnKeyReleased(event -> {
            if (name.getText().equals("")) {
                name.setText("unnamed");
            }
            currentLayer.changeShapeName(name.getText(), selectedShape);
            selectedShape = name.getText();
            lastShape = name.getText();
            updateUi();
        });
        shape.setItems(FXCollections.observableArrayList(
                "RECTANGLE", "CIRCLE"));
        shape.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> currentLayer.getShape(selectedShape).setType(Shape.ShapeType.valueOf(newValue)));
        width.setOnKeyReleased(event -> {
            currentLayer.getShape(selectedShape).setWidth(Float.parseFloat(width.getText()));
        });
        height.setOnKeyReleased(event -> {
            currentLayer.getShape(selectedShape).setHeight(Float.parseFloat(height.getText()));
        });
        x.setOnKeyReleased(event -> {
            currentLayer.getShape(selectedShape).setX(Float.parseFloat(x.getText()));
        });
        y.setOnKeyReleased(event -> {
            currentLayer.getShape(selectedShape).setY(Float.parseFloat(y.getText()));
        });
        rotation.setOnKeyReleased(event -> {
            currentLayer.getShape(selectedShape).setRotation(Float.parseFloat(rotation.getText()));
        });
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                canvas.setWidth(canvasPane.getWidth());
                canvas.setHeight(canvasPane.getHeight());
                updateCanvas();
            }
        };
        timer.start();
        updateUi();
    }

    public void addPolygon() {
        currentLayer.newShape("polygon", 0.5f, 0.5f, true);
        updateUi();
    }

    public void removePolygon() {
        int shapeIndex = currentLayer.getShapeOrder().indexOf(selectedShape);
        currentLayer.removeShape(selectedShape);
        if (currentLayer.getShapeKeySet().size() > 0) {
            if (currentLayer.getShapeKeySet().size() <= shapeIndex) {
                selectedShape = currentLayer.getShapeOrder().get(currentLayer.getShapeKeySet().size() - 1);
            } else {
                selectedShape = currentLayer.getShapeOrder().get(shapeIndex);
            }
        } else {
            selectedShape = "";
        }
        updateUi();
    }

    public void updateUi() {
        // update polygon list
        if (selectedShape.equals("")) {
            shapeData.setVisible(false);
            shapeData.setManaged(false);
        } else {
            shapeData.setVisible(true);
            shapeData.setManaged(true);
        }
        polygons.getChildren().clear();
        currentLayer.updateShapeOrder();
        currentLayer.getShapeOrder()
                .forEach(shapeKey -> {
                    Transform rectangle = currentLayer.getFrame(selectedFrame).getTransform(shapeKey);
                    try {
                        InputStream stream = Application.getStream("resources/fxml/polygon.fxml");
                        HBox item = new FXMLLoader().load(stream);
                        stream.close();
                        ((Label) item.lookup("#name")).setText(shapeKey);
                        Button visible = new Button("Hide");
                        Button hidden = new Button("Show");
                        visible.setOnMouseClicked(event -> {
                            item.getChildren().remove(visible);
                            item.getChildren().add(hidden);
                            // todo implement frame specific data
                            resize();
                        });
                        hidden.setOnMouseClicked(event -> {
                            item.getChildren().remove(hidden);
                            item.getChildren().add(visible);
                            resize();
                        });
                        Button moveUp = new Button("↑");
                        Button moveDown = new Button("↓");
                        moveUp.setOnMouseClicked(event -> {
                            List<String> order = currentLayer.getShapeOrder();
                            int temp = order.indexOf(shapeKey);
                            order.remove(shapeKey);
                            order.add(Math.max(temp - 1, 0), shapeKey);
                            currentLayer.updateShapeOrder(order);
                            updateUi();
                        });
                        moveDown.setOnMouseClicked(event -> {
                            List<String> order = currentLayer.getShapeOrder();
                            int temp = order.indexOf(shapeKey);
                            order.remove(shapeKey);
                            order.add(Math.min(temp + 1, order.size()), shapeKey);
                            currentLayer.updateShapeOrder(order);
                            updateUi();
                        });
                        item.getChildren().addAll(moveUp, moveDown, hidden);
                        item.setOnMouseClicked(event -> {
                            selectedShape = shapeKey;
                            updateUi();
                        });
                        Shape shape = currentLayer.getShape(selectedShape);
                        if (!selectedShape.equals(lastShape) && selectedShape.equals(shapeKey)) {
                            item.setBackground(new Background(new BackgroundFill(Color.rgb(32, 163, 214), CornerRadii.EMPTY, Insets.EMPTY)));
                            shapeData.setManaged(true);
                            name.setText(selectedShape);
                            this.shape.setValue(shape.getType().name());
                            width.setText("" + shape.getWidth());
                            height.setText("" + shape.getHeight());
                            x.setText("" + shape.getX());
                            y.setText("" + shape.getY());
                            rotation.setText("" + shape.getRotation());
                        }
                        polygons.getChildren().add(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        lastShape = selectedShape;
        resize();
    }

    public void updateCanvas() {
        entityAnimation.initializeRenderables();
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.clearRect(0, 0, canvas.getWidth() * 2, canvas.getHeight() * 2);
        List<Renderable> renderables = entityAnimation.getFrameRenderables(selectedFrame);
        for (Renderable renderable : renderables) {
            Transform t = renderable.getTransform();
            context.setFill(new Color(68/255.0, 93/255.0, 255/255.0, 1));
            double[] xPoints = new double[]{
                    t.xOffset,
                    t.xOffset + t.scaleX,
                    t.xOffset + t.scaleX,
                    t.xOffset
            };
            double[] yPoints = new double[]{
                    t.yOffset,
                    t.yOffset,
                    t.yOffset + t.scaleY,
                    t.yOffset + t.scaleY
            };
            if (t.rotOrigin) {
                rotatePoints(xPoints, yPoints, 2 * Math.PI * t.rotation, t.xOffset + t.scaleX / 2, t.yOffset + t.scaleY / 2);
            } else {
                rotatePoints(xPoints, yPoints, 2 * Math.PI * t.rotation, t.rotX, t.rotY);
            }
            scalePoints(yPoints, -1);
            scalePoints(xPoints, entityAnimation.width);
            scalePoints(yPoints, entityAnimation.height);
            double s = Math.min(canvas.getWidth() / entityAnimation.width, canvas.getHeight() / entityAnimation.height)/1.5;
            scalePoints(xPoints, s);
            scalePoints(yPoints, s);
            transformPoints(yPoints, canvas.getHeight());
            transformPoints(xPoints, canvas.getWidth() / 2 - s*entityAnimation.width / 2);
            transformPoints(yPoints, -canvas.getHeight() / 2 + s*entityAnimation.height / 2);
            context.fillPolygon(xPoints, yPoints, 4);
        }
    }

    public void transformPoints(double[] points, double amount) {
        for (int i = 0; i < points.length; i++) {
            points[i] = points[i] + amount;
        }
    }

    public void scalePoints(double[] points, double amount) {
        for (int i = 0; i < points.length; i++) {
            points[i] = points[i] * amount;
        }
    }

    public void rotatePoints(double[] pointsX, double[] pointsY, double angle, double pX, double pY) {
        for (int i = 0; i < pointsX.length; i++) {
            double s = Math.sin(angle);
            double c = Math.cos(angle);
            pointsX[i] -= pX;
            pointsY[i] -= pY;
            double xNew = pointsX[i] * c - pointsY[i] * s;
            double yNew = pointsX[i] * s + pointsY[i] * c;
            pointsX[i] = xNew + pX;
            pointsY[i] = yNew + pY;
//            pointsX[i] = Math.cos(angle) * (pointsX[i] - pX) - Math.sin(angle) * (pointsX[i] - pY) + pX;
//            pointsY[i] = Math.sin(angle) * (pointsX[i] - pX) + Math.cos(angle) * (pointsY[i] - pY) + pY;
        }
    }

    public void resize() {
        root.layout();
        root.applyCss();
        root.layout();
        root.applyCss();
    }
}

