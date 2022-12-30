package ru.netology.springbootkurs.repository;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RepoThreadChecWait {
    private static ConcurrentHashMap<String, Thread> repoThreadCheckWait = new ConcurrentHashMap<>();

    public static void addThread(String operationId, Thread thread) {
        repoThreadCheckWait.put(operationId, thread);
    }

    public static void spotWeit(String transferIdString) {
        repoThreadCheckWait.get(transferIdString).interrupt();
        repoThreadCheckWait.remove(transferIdString);
        log.info("ID_{}: Ожидание остановлено и удалено из списка операций", transferIdString);
    }
}
