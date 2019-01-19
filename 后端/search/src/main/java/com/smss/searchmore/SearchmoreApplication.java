package com.smss.searchmore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement//开启事务管理
@MapperScan("com.smss.searchmore.mapper")
public class SearchmoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchmoreApplication.class, args);
	}

}

