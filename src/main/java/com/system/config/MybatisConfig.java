package com.system.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.system.utils.annotations.SourceMapperScan;
import com.system.utils.annotations.TargetMapperScan;



@Configuration
public class MybatisConfig {

	/* 扫描**/
	@Bean
	public MapperScannerConfigurer sourceMapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sourceSqlSessionFactory");;
		mapperScannerConfigurer.setBasePackage("com.system.dao");
		mapperScannerConfigurer.setAnnotationClass(SourceMapperScan.class);
		return mapperScannerConfigurer;
	}
 
	@Bean
	public MapperScannerConfigurer targetMapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("targetSqlSessionFactory");
		mapperScannerConfigurer.setBasePackage("com.system.dao");
		mapperScannerConfigurer.setAnnotationClass(TargetMapperScan.class);
		return mapperScannerConfigurer;
	}



	
}