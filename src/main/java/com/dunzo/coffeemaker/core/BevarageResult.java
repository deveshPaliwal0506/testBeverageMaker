package com.dunzo.coffeemaker.core;

import java.util.Optional;

public class BevarageResult {

    private final boolean isBevarageCreated;
    private final String creationMessage;
    private final String bevarageName;

    public BevarageResult(boolean isBevarageCreated, String beverageName, String creationMessage) {
        this.isBevarageCreated = isBevarageCreated;
        this.creationMessage = creationMessage;
        this.bevarageName = beverageName;
    }

    public boolean isBevarageCreated() {
        return isBevarageCreated;
    }

    public String getCreationMessage() {
        return creationMessage;
    }

    public String getBevarageName() {
        return bevarageName;
    }

}
