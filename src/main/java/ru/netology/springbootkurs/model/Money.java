package ru.netology.springbootkurs.model;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class Money {
    private String currency;

    @Positive(message = "НЕПОДУСТИМАЯ СУММА")
    private Integer value;

    public Money (String currency, Integer value){
        this.currency = currency;
        this.value = value;
    }

}
