package ru.netology.springbootkurs.repository;

import ru.netology.springbootkurs.model.TransferCard;

import java.util.concurrent.ConcurrentHashMap;

public class RepoOperation {
    private static ConcurrentHashMap<String, TransferCard> repoOperation = new ConcurrentHashMap<>();
    private static int idOpretation = 0;

    public static boolean haveRepoOperation(String operationId) {
        return repoOperation.containsKey(operationId);
    }

    public static void delOperationId(String operationId) {
        repoOperation.remove(operationId);
    }

    public static String getNewID() {
        return Integer.toString(++idOpretation);
    }

    public static void addOperation(String operationId, TransferCard transferCard) {
        repoOperation.put(operationId, transferCard);
    }

    public static TransferCard giveTransferCard(String operationId) {
        return repoOperation.get(operationId);
    }
}
