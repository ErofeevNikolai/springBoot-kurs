package ru.netology.springbootkurs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import ru.netology.springbootkurs.model.Money;
import ru.netology.springbootkurs.model.OperationId;
import ru.netology.springbootkurs.model.TransferCard;
import ru.netology.springbootkurs.model.TransferId;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootKursApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;
    private static GenericContainer<?> beckApp = new GenericContainer<>("back").withExposedPorts(5500);
    private static TransferCard transferCard;
    private static Integer port;

    @BeforeAll
    public static void setUp() {
        //СОЗДАНИЕ ОБЕКТ, ДЛЯ ПЕРЕДАЧИ В ЗАПРОС
        Money money = new Money("RUB", 1_000);
        transferCard = new TransferCard(
                money, "111", "1111111111111111",
                "01/23", "2222222222222222"
        );

        //ЗАПУСКА КОНТЕЙНЕРА
        beckApp.start();

        //URL ТЕСТИРУЕМОГО ПРИЛОЖЕНИЯ
        port = beckApp.getMappedPort(5500);
    }


    @Test
    void transferOkTest() {

        //МЕТОД TRANSFER:
        String urlTransfer = "http://localhost:" + port + "/transfer";
        //ЗАПРОС В КОНТЕЙНЕР
        ResponseEntity<OperationId> forEntityPostTransfer = restTemplate.postForEntity(urlTransfer, transferCard, OperationId.class);
        //ПРОВЕРЯМ КОД ОТВЕТА
        assertEquals(forEntityPostTransfer.getStatusCodeValue(), 200);

        //МЕСТОД CONFIRM_OPERATION:
        //ПОЛУЧЕНИЕ iD ОПЕРАЦИИ И ФОМИРОАНИЕ ЗАПРОСА
        TransferId transferId = new TransferId(forEntityPostTransfer.getBody().getOperationId(), "0000");
        //URL
        String urlConfirmOperation = "http://localhost:" + port + "/confirmOperation";
        //ЗАПРОС В КОНТЕЙНЕР
        ResponseEntity<String> forEntityPostConfirmOperation = restTemplate.postForEntity(urlConfirmOperation, transferId, String.class);
        //ПРОВЕРЯМ КОД ОТВЕТА
        assertEquals(forEntityPostConfirmOperation.getStatusCodeValue(), 200);

        //СРАВНЕНИЕ РЕЗУЛЬТАТА
        assertEquals(forEntityPostConfirmOperation.getBody(), "ок");
    }

    @Test
    void transferErrorTest() {

        //МЕТОД TRANSFER:
        String urlTransfer = "http://localhost:" + port + "/transfer";
        //ЗАПРОС В КОНТЕЙНЕР
        ResponseEntity<OperationId> forEntityPostTransfer = restTemplate.postForEntity(urlTransfer, transferCard, OperationId.class);
        //ПРОВЕРКА КОДа ОТВЕТА
        assertEquals(forEntityPostTransfer.getStatusCodeValue(), 200);

        //МЕСТОД CONFIRM_OPERATION:
        //ПОЛУЧЕНИ iD ОПЕРАЦИИ И ФОМИРОАНИЕ ЗАПРОСА
        //КОДОМ ПОДТВЕРЖЕНИЯ ЗАДАН С ОШИБКОЙ
        TransferId transferId = new TransferId(forEntityPostTransfer.getBody().getOperationId(), "0001");
        //URL
        String urlConfirmOperation = "http://localhost:" + port + "/confirmOperation";
        //ЗАПРОС В КОНТЕЙНЕР
        ResponseEntity<String> forEntityPostConfirmOperation = restTemplate.postForEntity(urlConfirmOperation, transferId, String.class);
        //ПРОВЕРКА КОДА ОТВЕТА
        assertEquals(forEntityPostConfirmOperation.getStatusCodeValue(), 400);
        //СРАВНЕНИЕ РЕЗУЛЬТАТА
        assertEquals(forEntityPostConfirmOperation.getBody(), "Пароль не верен");
    }
}
