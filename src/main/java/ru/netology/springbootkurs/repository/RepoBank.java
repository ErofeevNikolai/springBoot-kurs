package ru.netology.springbootkurs.repository;

import lombok.extern.slf4j.Slf4j;
import ru.netology.springbootkurs.model.BankAccount;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RepoBank {
    private static ConcurrentHashMap<String, BankAccount> bank = new ConcurrentHashMap();

    public static void changeBalance(String cardFromNumber, int moneyValue) {
        BankAccount bankAccount = bank.get(cardFromNumber);
        bankAccount.setBalance(bankAccount.getBalance() + moneyValue);
        bank.put(cardFromNumber, bankAccount);
        log.info("БАНК: Карта: {}: изменение баланса на сумму: {}. Текущий баланс {}", cardFromNumber, moneyValue, bank.get(cardFromNumber).getBalance());
    }

    public static void addBankAccount(String cardNumber, String currency, String CVV, Integer balance) {
        bank.put(cardNumber, new BankAccount(cardNumber, currency, CVV, balance));
    }

    public static int getBalance(String cardNumber) {
        return bank.get(cardNumber).getBalance();
    }

    public static boolean repoContainsKey(String cardFromNumber) {
        return bank.containsKey(cardFromNumber);
    }

    public static boolean chekCVV(String cardFromNumber, String CVV) {
        return bank.get(cardFromNumber).getCVV().equals(CVV);
    }
}
