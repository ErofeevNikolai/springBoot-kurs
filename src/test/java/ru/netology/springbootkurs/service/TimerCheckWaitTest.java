package ru.netology.springbootkurs.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.springbootkurs.model.Money;
import ru.netology.springbootkurs.model.TransferCard;
import ru.netology.springbootkurs.repository.RepoBank;
import ru.netology.springbootkurs.repository.RepoOperation;

import static org.junit.jupiter.api.Assertions.*;


public class TimerCheckWaitTest {
    TimerCheckWait timerCheckWait;

    @BeforeEach
    void beforeStart() {
        TimerCheckWait.setTimeWorkTimer(1_000);
        timerCheckWait = new TimerCheckWait("1", "1111111111111111", 10_000);
        RepoBank.addBankAccount("1111111111111111", "12/23", "111", 10_000);

        Money money = new Money("RUR", 1_000);
        RepoOperation.addOperation("1", new TransferCard(money, "111","1111111111111111", "01/23", "2222222222222222"));
    }

    @Test
    public void testStartWeit() throws InterruptedException {
        timerCheckWait.startWeit();

        assertTrue(RepoOperation.haveRepoOperation("1"));
        Thread.sleep(1_500);
        assertFalse(RepoOperation.haveRepoOperation("1"));
        assertEquals(RepoBank.getBalance("1111111111111111"), 20_000);
    }
}

