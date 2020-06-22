package com.bobona.animationMaker.controllers;

import com.badlogic.gdx.Gdx;
import com.bobona.animationMaker.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MenuController implements Controller {

    Application application;

    @FXML HBox root;
    @FXML VBox main;
    @FXML VBox recent;
    @FXML VBox recentList;

    public void initialize(Application application) {
        this.application = application;
        this.application.userData.recentAnimations
                .forEach(recentAnimation -> {
                    try {
                        InputStream stream = new ByteArrayInputStream(Gdx.files.internal("resources/fxml/recentAnimation.fxml").readBytes());
                        GridPane item = new FXMLLoader().load(stream);
                        stream.close();
                        ((Label) item.lookup("#title")).setText(recentAnimation.name);
                        ((Label) item.lookup("#path")).setText(recentAnimation.path);
                        item.lookup("#remove").setOnMouseClicked(event -> {
                            application.userData.recentAnimations.remove(recentAnimation);
                            recentList.getChildren().remove(item);
                            resize();
                        });
                        item.setOnMouseClicked(event -> openAnimation(new File(((Label) item.lookup("#path")).getText())));
                        recentList.getChildren().add(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        resize();
    }

    public void resize() {
        // the second layout update is necessary
        root.layout();
        root.applyCss();
        root.layout();
        root.applyCss();
        main.setMinWidth(root.getWidth() - recent.getWidth());
    }

    public void create() throws IOException {
        application.createAnimation();
    }

    public void getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("APRI Animation Files", "*.apan"),
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(application.stages.get(Application.MENU_STAGE));
        openAnimation(file);
    }

    public void openAnimation(File file) {
        try {
            application.openAnimation(file);
        } catch (IOException e) {
            e.printStackTrace();
            application.notifyUser("Animation could not be opened.");
        }
    }
}
