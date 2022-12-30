package ru.netology.springbootkurs.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import ru.netology.springbootkurs.exception.InvalidCredentials;
import ru.netology.springbootkurs.model.OperationId;
import ru.netology.springbootkurs.model.TransferCard;
import ru.netology.springbootkurs.repository.RepoBank;
import ru.netology.springbootkurs.repository.RepoOperation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


class TransferServiceTest {
    TransferService transferService;


    @BeforeEach
    public void beforeTest() {
        RepoBank.addBankAccount("1111111111111111", "12/23", "111", 10_000);
        RepoBank.addBankAccount("2222222222222222", "12/23", "222", 10_000);
        transferService = new TransferService();
    }

    @Test
    void CardFromNumberTest() {
        TransferCard transferCard1 = Mockito.mock(TransferCard.class);
        when(transferCard1.getCardFromNumber()).thenReturn("1111111111111100");

        InvalidCredentials thrown = Assertions.assertThrows(InvalidCredentials.class, () -> transferService.checkTransfer(transferCard1));

        assertEquals(thrown.getMessage(), "Карты отправителя не существует");
    }

    @Test
    void CardToNumberTest() {
        TransferCard transferCard1 = Mockito.mock(TransferCard.class);
        when(transferCard1.getCardFromNumber()).thenReturn("1111111111111111");
        when(transferCard1.getCardToNumber()).thenReturn("2222222222222220");

        InvalidCredentials thrown = Assertions.assertThrows(InvalidCredentials.class, () -> transferService.checkTransfer(transferCard1));

        assertEquals(thrown.getMessage(), "Карты получателя не существует");
    }

    @Test
    void CVV_Test() {
        TransferCard transferCard1 = Mockito.mock(TransferCard.class);
        when(transferCard1.getCardFromNumber()).thenReturn("1111111111111111");
        when(transferCard1.getCardToNumber()).thenReturn("2222222222222222");
        when(transferCard1.getCardFromCVV()).thenReturn("110");

        InvalidCredentials thrown = Assertions.assertThrows(InvalidCredentials.class, () -> transferService.checkTransfer(transferCard1));

        assertEquals(thrown.getMessage(), "Указан некорректный CVV");
    }

    @Test
    void haveMoneyTest() {
        TransferCard transferCard1 = Mockito.mock(TransferCard.class);
        when(transferCard1.getCardFromNumber()).thenReturn("1111111111111111");
        when(transferCard1.getCardToNumber()).thenReturn("2222222222222222");
        when(transferCard1.getCardFromCVV()).thenReturn("111");
        when(transferCard1.getMoney()).thenReturn(11_000);

        InvalidCredentials thrown = Assertions.assertThrows(InvalidCredentials.class, () -> transferService.checkTransfer(transferCard1));

        assertEquals(thrown.getMessage(), "Недостаточно средств на счете карты отправителя");
    }

    @Test
    void checkTransfer() {
        int answerBalance = RepoBank.getBalance("1111111111111111");

        TransferCard transferCard1 = Mockito.mock(TransferCard.class);
        when(transferCard1.getCardFromNumber()).thenReturn("1111111111111111");
        when(transferCard1.getMoney()).thenReturn(1_000);
        when(transferCard1.getCardToNumber()).thenReturn("2222222222222222");
        when(transferCard1.getCardFromCVV()).thenReturn("111");

        OperationId operationId = new OperationId();
        operationId.setOperationId("1");

        //ПРОВЕКА ЗНАЧЕНИЯ ВОЗВРАЩАЕМОЕ МЕТОДОМ
        assertEquals(ResponseEntity.ok(operationId), transferService.checkTransfer(transferCard1));

        //ПРОВЕРКА БИЗМЕНЕНИЯ БАЛАНСА НА УКАЗАННУЮ СУММУ
        assertEquals(RepoBank.getBalance(transferCard1.getCardFromNumber()) + transferCard1.getMoney(), answerBalance);

        //ПРОВЕРКА НАЛИЧИЯ ССЫЛКИ НА ПОТОК В СПИСКЕ ОЖИДАНИЯ
        assertTrue(RepoOperation.haveRepoOperation("1"));
    }
}