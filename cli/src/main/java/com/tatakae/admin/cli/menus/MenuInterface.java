package com.tatakae.admin.cli.menus;

public interface MenuInterface {

    void display();

    Integer getChoice();

    MenuInterface getParent();

    boolean isValidChoice(final String choice);

}
