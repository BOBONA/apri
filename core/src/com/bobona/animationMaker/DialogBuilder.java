package com.bobona.animationMaker;

import com.badlogic.gdx.Gdx;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogBuilder {

    Node content;
    List<List<Object>> buttonsList;

    public DialogBuilder() throws IOException {
        InputStream stream = new ByteArrayInputStream(Gdx.files.internal("resources/fxml/alertDialog.fxml").readBytes());
        content = new FXMLLoader().load(stream);
        stream.close();
        ((Label) content.lookup("#message")).setText("");
        buttonsList = new ArrayList<>();
    }

    public DialogBuilder setMessage(String message) {
        ((Label) content.lookup("#message")).setText(message);
        return this;
    }

    public DialogBuilder setImage(String imagePath) {
        InputStream stream = Application.getStream(imagePath);
        ((ImageView) content.lookup("#imageContainer")).setImage(new Image(stream));
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public DialogBuilder setSubmitText(String text) {
        ((Button) content.lookup("#ok")).setText(text);
        return this;
    }

    public DialogBuilder addTextField(String label, String text) {
        addField(label, new TextField(text));
        return this;
    }

    public DialogBuilder addField(String label, Node field) {
        VBox vBox = (VBox) content.lookup("#input_fields");
        HBox hBox = new HBox(new Label(label), field);
        hBox.setAlignment(Pos.TOP_RIGHT);
        hBox.setSpacing(10);
        vBox.getChildren().add(hBox);
        return this;
    }

    public DialogBuilder addButton(String text, EventHandler<? super MouseEvent> handler, boolean closeWindow) {
        buttonsList.add(Arrays.asList(text, handler, closeWindow));
        return this;
    }

    public DialogBuilder addButton(String text) {
        buttonsList.add(Arrays.asList(text, (EventHandler<? super MouseEvent>) (event) -> {}, true));
        return this;
    }

    public Dialog build() {
        Dialog dialog = new Dialog();
        DialogPane dialogPane = new DialogPane();
        HBox buttons = (HBox) content.lookup("#buttons");
        for (List<Object> buttonInfo : buttonsList) {
            Button button = new Button((String) buttonInfo.get(0));
            button.setOnMouseClicked(event -> {
                ((EventHandler<? super MouseEvent>) buttonInfo.get(1)).handle(event);
                if ((Boolean) buttonInfo.get(2)) {
                    dialogPane.getScene().getWindow().hide();
                }
            });
            buttons.getChildren().add(button);
        }
        dialogPane.setContent(content);
        dialog.setDialogPane(dialogPane);
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
        return dialog;
    }
}
