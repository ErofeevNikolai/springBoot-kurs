package ru.netology.springbootkurs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.netology.springbootkurs.repository.RepoBank;

@Slf4j
@SpringBootApplication
public class SpringBootKursApplication {

    public static void main(String[] args) {
        //СОЗДАЕМ БАНКОВСКИЕ АКАУНТЫ
        RepoBank.addBankAccount("1111111111111111", "RUR", "111", 100_000);
        RepoBank.addBankAccount("2222222222222222", "RUR", "222", 200_000);
        RepoBank.addBankAccount("3333333333333333", "RUR", "333", 300_000);

        //ЗАПУСКАЕМ SPRING
        SpringApplication.run(SpringBootKursApplication.class, args);
    }
}
