package ru.netology.springbootkurs.model;

import lombok.Data;
/*
КЛАС ОПИСЫВАЮЩИЙ БАНКОВСКИЙ АКАУНТ
 */

@Data
public class BankAccount {
    private String cardNumber;
    private String currency;
    private String CVV;
    private Integer balance;


    public BankAccount(String cardNumber, String currency, String CVV, int balance) {
        this.cardNumber = cardNumber;
        this.currency = currency;
        this.CVV = CVV;
        this.balance = balance;
    }
}
