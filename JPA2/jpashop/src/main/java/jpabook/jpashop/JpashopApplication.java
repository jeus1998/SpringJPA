package jpabook.jpashop;

import jpabook.jpashop.hello.Hello;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
		Hello hello = new Hello();
		hello.setData("data");
		System.out.println("hello = " + hello);
	}

}
