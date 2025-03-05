package com.sg.flooringmastery.ui;

public enum MenuSelection {
    DISPLAY_ORDER(1),
    ADD_ORDER(2),
    EDIT_ORDER(3),
    REMOVE_ORDER(4),
    EXPORT_ALL_DATA(5),
    EXIT(6);

    private final int value;

    MenuSelection(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MenuSelection fromInt(int i) {
        for (MenuSelection selection : values()) {
            if (selection.getValue() == i) {
                return selection;
            }
        }
        return null;
    }
}
