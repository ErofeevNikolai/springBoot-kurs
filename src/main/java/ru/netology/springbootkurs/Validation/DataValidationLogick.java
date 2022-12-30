package ru.netology.springbootkurs.Validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataValidationLogick implements ConstraintValidator<DateValidation, String> {
    private static final int MAX_CARD_VALIDITY_PERIOD = 5;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (s.length() < 4) {
            return false;
        }
        if (!s.contains("/")) {
            return false;
        }

        //ПОЛУЧАЕМ ЗНАЧЕНИЯ ТЕКУЩЕЙ ДАТЫ
        int currentYear = Integer.parseInt(new SimpleDateFormat("yy").format(new Date()));
        int currentMounth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));

        //ПОЛУЧВАЕМ ЗАНЧЕНИЕ МЕСЯЦА И ПРОВЕРЯЕМОЙ ДАТЫ
        String data[] = s.split("/");
        int verificationMonth = Integer.parseInt(data[0]);
        int verificationYear = Integer.parseInt(data[1]);

        //ПРОВЕРКА КОРРРЕКТНОСТИ ФОРМАТА МЕСЯЦА
        if (verificationMonth < 1 || 12 < verificationMonth) {
            return false;
        }
        //ПРОВЕКА СРОКА ДЕЙСТВИЯ КАРТЫ ПО ГОДУ
        if (verificationYear < currentYear) {
            return false;
        }

        //ПРОВЕКРА СТРОКА ДЕЙСТВИЯ КАРТЫ  МЕСЯЦУ И ГОДУ (ЕСЛИ ГОД ПРОВЕРЯМОГО ЗНАЧЕНИЯ СОВПАДЕТ С ТЕКУЩИМ)
        if (verificationYear == currentYear && verificationMonth < currentMounth) {
            return false;
        }

        //ПРОВЕРКА АДЕКВАТНОСТИ СРОКА ДЕЙТВИЯ КАРТЫ
        if (verificationYear > (currentYear + MAX_CARD_VALIDITY_PERIOD)) {
            return false;
        }

        return true;
    }
}
