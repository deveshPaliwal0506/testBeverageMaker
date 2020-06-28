package com.dunzo.coffeemaker.configuration;

import com.dunzo.coffeemaker.core.BevarageResult;
import com.dunzo.coffeemaker.services.BevarageOrderOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.dunzo")
public class BevarageApplication {

    @Autowired
    private BevarageOrderOperator operator;

    public static void main(String[] args) {
        SpringApplication.run(BevarageApplication.class, args);
    }

   /* // Put your logic here.
    @Override
    public void run(String... args) throws Exception {

       List<BevarageResult> result = operator.initiateBevarageCreation();

       System.out.println(result);

    }*/


}
