package com.tatakae.admin.cli.menus;

public interface Menu {

    public void display();

    public int getChoice();

    public boolean isValidChoice(final String choice);

}
