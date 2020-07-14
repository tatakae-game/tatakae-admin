package com.tatakae.admin.cli.menus;

public interface MenuInterface {

    void display();

    Integer getChoice();

    boolean isValidChoice(final String choice);

}
