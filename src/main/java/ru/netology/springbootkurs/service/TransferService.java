package ru.netology.springbootkurs.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.netology.springbootkurs.exception.InvalidCredentials;
import ru.netology.springbootkurs.model.OperationId;
import ru.netology.springbootkurs.model.TransferCard;
import ru.netology.springbootkurs.model.TransferId;
import ru.netology.springbootkurs.repository.RepoBank;
import ru.netology.springbootkurs.repository.RepoOperation;
import ru.netology.springbootkurs.repository.RepoThreadChecWait;

@Slf4j
@Service
@AllArgsConstructor
public class TransferService {
    private final String INPUT_CODE_VERIFICATION = "0000";

    public ResponseEntity<OperationId> checkTransfer(TransferCard transferCard) {
        String cardFromNumber = transferCard.getCardFromNumber();
        Integer moneyValue = transferCard.getMoney();

        // ПРОВЕРКА СУЩЕСТВОВАНИЯ КАРТ В БАНКЕ
        if (!RepoBank.repoContainsKey(cardFromNumber)) {
            throw new InvalidCredentials("Карты отправителя не существует");
        }
        if (!RepoBank.repoContainsKey(transferCard.getCardToNumber())) {
            throw new InvalidCredentials("Карты получателя не существует");
        }

        //ПРОВЕРКА CVV
        if (!RepoBank.chekCVV(cardFromNumber, transferCard.getCardFromCVV())) {
            throw new InvalidCredentials("Указан некорректный CVV");
        }

        //ПРОВЕРКА БАЛАНСА КАРТЫ ОТПРАВИТЕЛЯ НА ВОЗМОЖНОСТЬ СОВЕРШЕНИЯ ПЕРЕВОДА
        if (moneyValue > (RepoBank.getBalance(cardFromNumber))) {
            throw new InvalidCredentials("Недостаточно средств на счете карты отправителя");
        }

        log.info("Проверка реквизитов пройдена");

        //ПРИСВАЕМ ТРАНЗАКЦИИ ID
        OperationId operationId = new OperationId();
        operationId.setOperationId(RepoOperation.getNewID());
        String operationIdToString = operationId.getOperationId();

        log.info("ID_{}: Заморозка средств на карте: " + cardFromNumber + ".", operationIdToString);
        //ЗАМОРАЖИВАЕМ СРЕДСВА НА КАРТЕ ОТПРАВИТЕЛЯ.
        RepoBank.changeBalance(cardFromNumber, -moneyValue);

        //ГЕНЕРИМ ПРОВЕРОЧНЫЙ КОД
        transferCard.setVerification(String.valueOf((999 + (int) (Math.random() * (9999 - 999 + 1)))));
        log.info("ID_{}: Создан проверочный код.", operationIdToString);

        //ПОМЕЩАЕМ ФАЙЛ TRANSFERRED В ХРАНИЛИЩЕ
        RepoOperation.addOperation(operationIdToString, transferCard);

        //ЗАПУСКАЕМ ТАЙМЕР ОЖИДАНИЯ
        new TimerCheckWait(operationIdToString, cardFromNumber, moneyValue).startWeit();

        //ВОЗВРАЩАЕМ ОК С ID ОПЕРАЦИИ
        return ResponseEntity.ok(operationId);

    }

    //ВЕРТИФИКАЦИЯ
    public ResponseEntity<String> confirmOperation(TransferId transferId) {
        String operationId = transferId.getOperationId();

        if (transferId.getCode().equals(INPUT_CODE_VERIFICATION)) {
            log.info("ID_{}: Кода принят", operationId);
            String transferIdString = transferId.getOperationId();

            //ОСТАНАВЛИВАЕМ ТАЙМЕР ОЖДИДАНИЯ
            RepoThreadChecWait.spotWeit(transferIdString);

            //ВЫПОЛНЯЕМ ЗАЧИСЛЕНИЕ НА КАРТУ ПОЛУЧАТЕЛЯ
            log.info("ID_{}: Зачисление денежных средств на карту получателя", operationId);

            TransferCard transferCard = RepoOperation.giveTransferCard(transferIdString);
            RepoBank.changeBalance(transferCard.getCardToNumber(), transferCard.getAmount().getValue());

            return ResponseEntity.ok("ок");
        } else {
            log.info("ID_{}: Код указан неверно", operationId);
            return new ResponseEntity("Пароль не верен", HttpStatus.BAD_REQUEST);
        }
    }
}
