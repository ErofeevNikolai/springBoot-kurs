package ru.netology.springbootkurs.exception;

import ru.netology.springbootkurs.model.TransferCard;

public class InvalidCredentials extends IllegalArgumentException {
    private TransferCard transferCard;

    public InvalidCredentials(String msg) {
        super(msg);
    }
}
