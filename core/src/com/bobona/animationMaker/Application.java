package com.bobona.animationMaker;

import com.badlogic.gdx.Gdx;
import com.bobona.animationMaker.controllers.Controller;
import com.bobona.animationMaker.controllers.EditorController;
import com.bobona.animationMaker.controllers.MenuController;
import com.bobona.apri.visual.animation.EntityAnimation;
import com.bobona.apri.visual.animation.GsonEntityAnimation;
import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Application extends javafx.application.Application {

    public Map<String, Stage> stages;
    public UserData userData;
    Gson gson;

    public static Application application;
    public static final String MENU_STAGE = "menu";
    public static final String EDITOR_STAGE = "editor";
    public static final int DEFAULT_FRAME_COUNT = 5;

    @Override
    public void start(Stage stage) throws Exception {
        this.stages = new HashMap<>();
        gson = new Gson();
        application = this;
        try {
            userData = new UserData();
        } catch (Exception e) {
            e.printStackTrace();
            notifyUser("Failed to load user data.");
        }
        MenuController controller = (MenuController) createStage(
                "resources/fxml/menu.fxml",
                "resources/css/menu.css",
                450f, 275f,
                "Choose animation:",
                MENU_STAGE, null, false);
        controller.initialize(this);
    }

    @Override
    public void stop() throws Exception {
        Gdx.app.exit();
        super.stop();
    }

    public Controller createStage(String fxmlPath, String cssPath, float width, float height, String stageTitle, String stageName, String currentStageName, boolean replaceStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        InputStream stream = new ByteArrayInputStream(Gdx.files.internal(fxmlPath).readBytes());
        Scene scene = new Scene(loader.load(stream), width, height);
        stream.close();
        scene.getStylesheets().add(Gdx.files.internal(cssPath).readString());
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.setScene(scene);
        stage.widthProperty().addListener((obs, oldVal, newVal) -> ((Controller) loader.getController()).resize());
        stage.heightProperty().addListener((obs, oldVal, newVal) -> ((Controller) loader.getController()).resize());
        stage.show();
        stages.put(stageName, stage);
        if (replaceStage) {
            stages.get(currentStageName).hide();
        }
        return loader.getController();
    }

    public void createAnimation() throws IOException {
        EntityAnimation animation = new EntityAnimation(new GsonEntityAnimation(), new HashMap<>(), false);
        animation.addLayer("main", DEFAULT_FRAME_COUNT);
        openAnimation(animation);
    }

    public void openAnimation(EntityAnimation animation) throws IOException {
        EditorController controller = (EditorController) createStage(
                "resources/fxml/editor.fxml",
                "resources/css/menu.css",
                450f, 275f,
                animation.getName(),
                EDITOR_STAGE, MENU_STAGE, true);
        controller.initialize(this, animation);
    }

    public void openAnimation(File file) throws IOException {
        String[] pathList = file.getPath().split("\\\\");
        pathList = Arrays.copyOfRange(pathList, 3, pathList.length);
        String path = String.join("\\", pathList);
        String json = Gdx.files.external(path).readString();
        EntityAnimation animation = new EntityAnimation(gson.fromJson(json, GsonEntityAnimation.class), new HashMap<>(), false);
        RecentAnimation recentAnimation = new RecentAnimation(
                animation.getName() != null ? animation.getName() : file.getPath(),
                file.getPath());
        userData.recentAnimations.remove(recentAnimation);
        userData.recentAnimations.add(recentAnimation);
        openAnimation(animation);
    }

    public void notifyUser(String message) {
        try {
            new DialogBuilder()
                    .setImage("resources/img/warning.jpg")
                    .setMessage(message)
                    .addButton("Ok")
                    .build()
                    .show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InputStream getStream(String path) {
        return new ByteArrayInputStream(Gdx.files.internal(path).readBytes());
    }
}
