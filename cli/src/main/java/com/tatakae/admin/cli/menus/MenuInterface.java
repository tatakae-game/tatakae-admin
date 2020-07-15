package com.tatakae.admin.cli.menus;

public interface MenuInterface {

    void run();

    boolean isValidChoice(final String choice, final Integer min, final Integer max);

    void loadChoice(final Integer choice);

}
