package com.system.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
@EnableTransactionManagement
@Configuration
public class DBSyncConfig implements TransactionManagementConfigurer{

	
	
	
	/**   两个SqlSessionFactoryBean设置  **/
	@Bean
	@Qualifier("sourceSqlSessionFactory")
	public SqlSessionFactory sourceSqlSessionFactory(@Autowired @Qualifier("sourceDataSource") DataSource sourceDataSource) {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(sourceDataSource);
		org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
		conf.setMapUnderscoreToCamelCase(true);
		conf.setLogPrefix("dao");
		sqlSessionFactoryBean.setConfiguration(conf);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(); 
		try {
			sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml") );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			return  sqlSessionFactoryBean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Bean
	@Qualifier("targetSqlSessionFactory")
	public SqlSessionFactory targetSqlSessionFactory(@Autowired @Qualifier("targetDataSource") DataSource targetDataSource) {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(targetDataSource);
		org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
		conf.setMapUnderscoreToCamelCase(true);
		conf.setLogPrefix("dao");
		sqlSessionFactoryBean.setConfiguration(conf);
		
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(); 
		try {
			sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml") );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			return  sqlSessionFactoryBean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**   两个SqlSession 设置  **/
	@Bean
	@Scope("prototype")
	@Qualifier("sourceSqlSessionTemplate")
	public SqlSessionTemplate sourceSqlSessionTemplate(@Autowired @Qualifier("sourceSqlSessionFactory") SqlSessionFactory sourceSqlSessionFactory) {
		return new SqlSessionTemplate(sourceSqlSessionFactory);
		
	}
	@Bean
	@Scope("prototype")
	@Qualifier("targetSessionTemplate")
	public SqlSessionTemplate targetSessionTemplate(@Autowired @Qualifier("targetSessionFactory") SqlSessionFactory targetSessionFactory) {
		return new SqlSessionTemplate(targetSessionFactory);
	}
	
	@Autowired 
	@Qualifier("targetDataSource") 
	DataSource targetDataSource ; 
	@Autowired
	@Qualifier("sourceDataSource") 
	DataSource sourceDataSource ; 
	
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		DataSourceTransactionManager dtm1 = new DataSourceTransactionManager(sourceDataSource);
		DataSourceTransactionManager dtm2 = new DataSourceTransactionManager(targetDataSource);
		ChainedTransactionManager ctm = new ChainedTransactionManager(dtm1, dtm2);
		return ctm;
	} 
	
}
