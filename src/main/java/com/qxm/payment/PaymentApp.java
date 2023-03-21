package com.qxm.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.qxm.payment.domain.model.entity.User;
import com.qxm.payment.domain.repository.UserRepository;

/**
*
* @author Qu Xiaoming
*/
@SpringBootApplication
public class PaymentApp  implements CommandLineRunner
{
	@Autowired
	private UserRepository userrepository;
    public static void main( String[] args )
    {
    	SpringApplication.run(PaymentApp.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		userrepository.save(new User("jack", 
				"$2a$10$NVM0n8ElaRgg7zWO1CxUdei7vWoPg91Lz2aYavh9.f9q0e4bRadue"));
		userrepository.save(new User("mike", 
				"$2a$10$8cjz47bjbR4Mn8GMg9IZx.vyjhLXR/SKKMSZ9.mP9vpMu0ssKi8GW"));
		
	}
}
