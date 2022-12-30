package ru.netology.springbootkurs.Validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
//Указывает, какой элемент программы будет использоваться аннотацией. В нашем случае мы указываем, что аннотация будет применима к полям (FIELD
@Target(ElementType.FIELD)
@Constraint(validatedBy = DataValidationLogick.class)
public @interface DateValidation {
    String message() default "{УКАЗАН НЕВЕРНЫЙ ФОРМАТ СРОКА ДЕЙСТВИЯ КАРТЫ}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

