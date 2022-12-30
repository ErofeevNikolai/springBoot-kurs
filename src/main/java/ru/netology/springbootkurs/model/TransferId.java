package ru.netology.springbootkurs.model;

import lombok.Data;

@Data
public class TransferId {
    private String operationId;
    private String code;

    public TransferId(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

}
