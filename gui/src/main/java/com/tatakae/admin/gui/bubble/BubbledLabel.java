package com.tatakae.admin.gui.bubble;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class BubbledLabel extends Label {

    private BubbleSpec bs = BubbleSpec.FACE_LEFT_CENTER;
    private double padding = 5.0;
    private boolean systemCall = false;

    public BubbledLabel() {
        super();
        init();
    }

    public BubbledLabel(String arg0, Node arg1) {
        super(arg0, arg1);
        init();
    }

    public BubbledLabel(String arg0) {
        super(arg0);
        init();
    }

    public BubbledLabel(BubbleSpec bubbleSpec) {
        super();
        this.bs = bubbleSpec;
        init();
    }

    public BubbledLabel(String arg0, Node arg1,BubbleSpec bubbleSpec) {
        super(arg0, arg1);
        this.bs = bubbleSpec;
        init();
    }

    public BubbledLabel(String arg0,BubbleSpec bubbleSpec) {
        super(arg0);
        this.bs = bubbleSpec;
        init();
    }

    private void init(){
        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.3);
        ds.setOffsetY(1.3);
        ds.setColor(Color.DARKGRAY);
        setPrefSize(Label.USE_COMPUTED_SIZE, Label.USE_COMPUTED_SIZE);
        shapeProperty().addListener(new ChangeListener<Shape>() {
            @Override
            public void changed(ObservableValue<? extends Shape> arg0,
                                Shape arg1, Shape arg2) {
                if(systemCall){
                    systemCall = false;
                }else{
                    shapeIt();
                }
            }
        });

        heightProperty().addListener(arg0 -> {
            if(!systemCall)
                setPrefHeight(Label.USE_COMPUTED_SIZE);
        });

        widthProperty().addListener(observable -> {
            if(!systemCall)
                setPrefHeight(Label.USE_COMPUTED_SIZE);
        });

        shapeIt();
    }

    @Override
    protected void updateBounds() {
        super.updateBounds();

        switch (bs) {
            case FACE_LEFT_BOTTOM:
                setPadding(new Insets(padding,padding,
                        (this.getBoundsInLocal().getWidth() * ((Bubble)getShape()).drawRectBubbleIndicatorRule) / 2
                                + padding, padding));
                break;
            case FACE_LEFT_CENTER:
                setPadding(new Insets(padding,padding,padding,
                        (this.getBoundsInLocal().getWidth() * ((Bubble)getShape()).drawRectBubbleIndicatorRule) / 2
                                + padding));
                break;
            case FACE_RIGHT_BOTTOM:
            case FACE_RIGHT_CENTER:
                setPadding(new Insets(padding,
                        (this.getBoundsInLocal().getWidth() * ((Bubble)getShape()).drawRectBubbleIndicatorRule) / 2
                                + padding, padding, padding));
                break;
            case FACE_TOP:
                setPadding(new Insets(
                        (this.getBoundsInLocal().getWidth() * ((Bubble)getShape()).drawRectBubbleIndicatorRule) / 2
                                + padding, padding, padding, padding));
                break;

            default:
                break;
        }
    }

    public final double getpadding() {
        return padding;
    }

    public void setpadding(double padding) {
        if(padding > 25.0)
            return;
        this.padding = padding;
    }

    public BubbleSpec getBubbleSpec() {
        return bs;
    }

    public void setBubbleSpec(BubbleSpec bubbleSpec) {
        this.bs = bubbleSpec;
        shapeIt();
    }

    private void shapeIt(){
        systemCall = true;
        setShape(new Bubble(bs));
        System.gc();
    }
}
