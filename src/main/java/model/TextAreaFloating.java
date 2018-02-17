package main.java.model;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class TextAreaFloating extends TextArea {
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;

    public TextAreaFloating(){
        super();

    }

    public void setSettings(){

        Node textAreaContent = this.lookup(".content");
        textAreaContent.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {

            System.out.println("is clicked");

            orgSceneX = e.getSceneX();
            orgSceneY = e.getSceneY();
            orgTranslateX = this.getTranslateX();
            orgTranslateY = this.getTranslateY();

            this.toFront();
        });

        textAreaContent.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {

            System.out.println("is dragged");

            double offsetX = e.getSceneX() - orgSceneX;
            double offsetY = e.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            this.setTranslateX(newTranslateX);
            this.setTranslateY(newTranslateY);
        });
    }



}
