package ru.netology.springbootkurs.service;

import lombok.extern.slf4j.Slf4j;
import ru.netology.springbootkurs.repository.RepoBank;
import ru.netology.springbootkurs.repository.RepoOperation;
import ru.netology.springbootkurs.repository.RepoThreadChecWait;

@Slf4j


// ТАЙМЕР ВРЕМЕНИ НА ВВЕДЕНИЕ ПРОВЕРОЧНОГО КОДА
public class TimerCheckWait implements Runnable {

    private static int timeWorkTimer = 10_000;
    private String operationId;
    private Integer moneyValue;
    private String cardFromNumber;
    private Thread thread = new Thread(this);


    public TimerCheckWait(String operationIdToString, String cardFromNumber, Integer moneyValue) {
        this.operationId = operationIdToString;
        this.cardFromNumber = cardFromNumber;
        this.moneyValue = moneyValue;
    }


    //ЗАПУСК ТАЙМЕРА
    public void startWeit() {
        thread.start();
        log.info("ID_{}: Запуск таймера ожидания кода.", operationId);
        RepoThreadChecWait.addThread(operationId, thread);
    }

    //ОПИСВНИЕ РАБОТЫ ТАЙМЕРА
    @Override
    public void run() {
        if (RepoOperation.haveRepoOperation(operationId)) {
            try {
                thread.sleep(timeWorkTimer);
            } catch (Exception e) {
                return;
            }
            RepoOperation.delOperationId(operationId);
        }
        log.info("ID_{}: Время ожидания кода вышло.", operationId);
        log.info("ID_{}: Возврат денежных средств на карту отправителя.", operationId);
        RepoBank.changeBalance(cardFromNumber, moneyValue);
        thread.interrupt();

    }

    public static void setTimeWorkTimer(int time){
        timeWorkTimer = time;
    }
}
