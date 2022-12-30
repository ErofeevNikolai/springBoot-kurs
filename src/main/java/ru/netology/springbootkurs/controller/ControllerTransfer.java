package ru.netology.springbootkurs.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.netology.springbootkurs.exception.InvalidCredentials;
import ru.netology.springbootkurs.model.OperationId;
import ru.netology.springbootkurs.model.TransferCard;
import ru.netology.springbootkurs.model.TransferId;
import ru.netology.springbootkurs.service.TransferService;

import java.util.Objects;

@Slf4j
@RestController
@CrossOrigin()          // разрешение любому адресу предоставлять доступ к серверу
@AllArgsConstructor
public class ControllerTransfer {
    private final TransferService transferService;

    @PostMapping("/transfer")
    private ResponseEntity<OperationId> transfer(@RequestBody @Validated TransferCard transferCard) {
        log.info("POST//transfer: Запрос на перевод средств");
        return transferService.checkTransfer(transferCard);
    }

    @PostMapping("/confirmOperation")
    private ResponseEntity<String> confirmOperation(@RequestBody TransferId perationId) {
        log.info("POST/confirmOperation: ID_{}: Ввод проверочного кода", perationId.getOperationId());
        return transferService.confirmOperation(perationId);
    }

    @ExceptionHandler
    public ResponseEntity<String> excepUnauthorizedUser(InvalidCredentials e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> exep(MethodArgumentNotValidException e) {
        //ПОЛУЧАЕМ ЗАДАННОЕ СООБЩЕНИЕ ПРИ ОШИБКИ ВАЛИДАЦИИ ПОЛЕЙ
        String validationMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("Ошибка создания экземпляра класса {}  в связи с: {}", e.getObjectName(), validationMessage);
        return new ResponseEntity<>(validationMessage, HttpStatus.BAD_REQUEST);
    }

}