package com.system.config;

import com.system.utils.annotations.SourceMapperScan;
import com.system.utils.annotations.TargetMapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@Configuration
@Order(5)
public class MybatisConfig {

	/* 扫描**/
	@Bean
	public static MapperScannerConfigurer sourceMapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sourceSqlSessionFactory");;
		mapperScannerConfigurer.setBasePackage("com.system.dao");
		mapperScannerConfigurer.setAnnotationClass(SourceMapperScan.class);
		return mapperScannerConfigurer;
	}
 
	@Bean
	public static MapperScannerConfigurer targetMapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("targetSqlSessionFactory");
		mapperScannerConfigurer.setBasePackage("com.system.dao");
		mapperScannerConfigurer.setAnnotationClass(TargetMapperScan.class);
		return mapperScannerConfigurer;
	}



	
}