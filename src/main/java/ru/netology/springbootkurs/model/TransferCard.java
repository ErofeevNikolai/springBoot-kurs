package ru.netology.springbootkurs.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.netology.springbootkurs.Validation.DateValidation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


//КЛАСС DTO ПЕРЕВОДА ДЕНГ
@Data
public class TransferCard {
    @NotNull
    private Money amount;

    @Length(min = 3, max = 3, message = "УКАЗАН НЕВЕРНЫЙ CVC")
    private String cardFromCVV;

    @Size(min = 16, max = 16, message = "УКАЗАН НЕВЕРНЫЙ НОМЕР КАРТЫ ОТПРАВИТЕЛЯ")
    private String cardFromNumber;

    @DateValidation
    private String cardFromValidTill;

    @Size(min = 16, max = 16, message = "УКАЗАН НЕКОРРЕКТНЫЙ НОМЕР КАРТЫ ПОЛУЧАТЕЛЯ")
    private String cardToNumber;

    private String verification;

    public TransferCard(Money amount, String cardFromCVV, String cardFromNumber,String cardFromValidTill, String cardToNumber ){
        this.amount = amount;
        this.cardFromCVV = cardFromCVV;
        this.cardFromNumber = cardFromNumber;
        this.cardFromValidTill = cardFromValidTill;
        this.cardToNumber = cardToNumber;
    }

    public Integer getMoney(){
        return amount.getValue();
    }

}