package com.antifraud;

import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.antifraud.mapper") // 扫描 Mapper 接口
public class BackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);


		System.out.println("====== 老年人反诈平台后端启动成功 ======");
	}
}
