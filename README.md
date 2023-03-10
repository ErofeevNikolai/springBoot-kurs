# Сервис перевода денег

## Описание
Приложение представляет собой REST-сервис и предназначен для быстрого и удобного перевода денежных средств между держателями банковских карт;
при этом данные всех операций, произведенные в сервисе *MoneyTransferService* (с указанием времени операции) сохраняются в файле app.log.

## Порядок использования
### Запуск приложения
Вариант 1:
Для запуска приложения необходимо в корневом каталоге запустить файл springBoot-kurs-0.0.1-SNAPSHOT
с помощью команды java -jar /springBoot-kurs-0.0.1-SNAPSHOT.jar.

Вариант 2:
Также существует возможность запуска сервиса с помощью приложения *Docker* - для этого необходимо в корневом каталоге
запустить команду создания контейнера из образа back на порту 5500. (Dockerfile).

### Запуск клиентского WEB-приложения 
Для перевода денежных средств необходимо перейти по ссылке https://serp-ya.github.io/card-transfer/


## Перевод денежных средств
Процесс перевода осуществляется в два этапа:

##### 1. Этап. Ввод и проверка данных:
Для перевода средств требуется заполнить следующие поля:
- номер карты списания, состоящий из 16-и символов (например, 1111 1111 1111 1111);
- номер карты зачисления, состоящий из 16-и символов (например, 2222 2222 2222 2222) - обязательный параметр, номер карты списания не может совпадать с номером карты зачисления;
- дата окончания действия карты списания в формате ММ/ГГ (например 11/22) - обязательный параметр, дата не может быть ниже текущей, месяц не может быть ниже 1 и выше 12, срок действия карты не может быть больше чем текущий + 5 лет 
- код проверки подлинности карты CVV, состоящий из 3-х символов (например, 111);
- сумма перевода в валюте перевода (RUR), сумма перевода должна быть целым положительным числом.
- 
Данные отправляются на сервер, где происходит проверка валидности этих данных, учитывая существования карт, проверки указанного срока и CVV наличие необходимой для перевода суммы денежных средств.  
На карте списания. Сумма перевода замораживается, генерируется код подтверждения. Запускается таймер ожидания ввода кода. 

#### 2. Этап. Подтверждение операции:
На втором этапе перевода сервер проверяет проверочный код, и при его корректности происходит зачисление средств на карту отправителя
отправляется на сервер, где происходит его проверка,
и в случае успеха, сумма замороженных средств зачислятся на карту получателя.
Если в заданный промежуток времени клиент не вводит корректный код, замороженные деньги возвращаются на карту отправителя, операция отменяется. 

При возникновении ошибок на стороне сервера или при наличии ошибочных входных данных сервер выдает ошибку с пояснениями.