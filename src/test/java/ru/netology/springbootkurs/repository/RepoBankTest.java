package ru.netology.springbootkurs.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepoBankTest {

    @Test
    void changeBalance() {
        RepoBank.addBankAccount("1111111111111111", "12/22", "404", 0);
        RepoBank.changeBalance("1111111111111111", 1);

        assertEquals(RepoBank.getBalance("1111111111111111"), 1);
    }
}