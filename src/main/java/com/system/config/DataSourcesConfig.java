package com.system.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.system.config.support.MultiSourceDataSource;

@Configuration
public class DataSourcesConfig implements ApplicationContextAware {

	@Autowired
	private DatabasesNames databasesNames ; 
	
	
	private ApplicationContext applicationContext ; 
	/**   两个数据源设置  **/
	@Bean(name="sourceDataSource_shuangshi") 
	@ConfigurationProperties(prefix="sync_source_shuangshi.datasource")
	@Qualifier("sourceDataSource_shuangshi")
	public DataSource sourceDataSource() {
		 DataSourceBuilder create = DataSourceBuilder.create();
		 return create.build();
	}
	
	@Bean(name="targetDataSource_shuangshi")
	@ConfigurationProperties(prefix="sync_target_shuangshi.datasource")
	@Qualifier("targetDataSource_shuangshi")
	public DataSource targetDataSource_shuangshi() {
		return DataSourceBuilder.create().build();
	}
	
	/**   两个数据源设置  **/
	@Bean(name="sourceDataSource_shuangshi-assistant") 
	@ConfigurationProperties(prefix="sync_source_shuangshi-assistant.datasource")
	@Qualifier("sourceDataSource_shuangshi-assistant")
	public DataSource sourceDataSource_shuangshi_assistant() {
		 DataSourceBuilder create = DataSourceBuilder.create();
		 return create.build();
	}
	
	@Bean(name="targetDataSource_shuangshi-assistant") 
	@ConfigurationProperties(prefix="sync_target_shuangshi-assistant.datasource")
	@Qualifier("targetDataSource_shuangshi-assistant")
	public DataSource targetDataSource_shuangshi_assistant() {
		DataSource build = DataSourceBuilder.create().build();
		return build ; 
	}
	
	/**   两个数据源设置  **/
	@Bean(name="sourceDataSource_xxl-job") 
	@ConfigurationProperties(prefix="sync_source_xxl-job.datasource")
	@Qualifier("sourceDataSource_xxl-job")
	public DataSource sourceDataSource_xxl_job() {
		  DataSource build = DataSourceBuilder.create().build();
		 return build;
	}
	
	@Bean(name="targetDataSource_xxl-job") 
	@ConfigurationProperties(prefix="sync_target_xxl-job.datasource")
	@Qualifier("targetDataSource_xxl-job")
	public DataSource targetDataSource_xxl_job() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@Qualifier("sourceDataSource")
	public MultiSourceDataSource sourceMultiSourceDataSource() {
		MultiSourceDataSource multiSourceDataSource = new MultiSourceDataSource();
		Map<Object, Object> targetDataSources = new HashMap<>();
		List<String> names = databasesNames.getNames();
		for (String name : names) {
			DataSource bean = (DataSource)applicationContext.getBean("sourceDataSource_"+name);
			targetDataSources.put(name, bean);
		}
		multiSourceDataSource.setTargetDataSources(targetDataSources );
		DataSource bean = (DataSource)applicationContext.getBean("sourceDataSource_"+names.get(0));
		multiSourceDataSource.setDefaultTargetDataSource(bean);
		return multiSourceDataSource;
	}
	
	@Bean
	@Qualifier("targetDataSource")
	public MultiSourceDataSource targetMultiSourceDataSource() {
		MultiSourceDataSource multiSourceDataSource = new MultiSourceDataSource();
		Map<Object, Object> targetDataSources = new HashMap<>();
		List<String> names = databasesNames.getNames();
		for (String name : names) {
			DataSource bean = (DataSource)applicationContext.getBean("targetDataSource_"+name);
			targetDataSources.put(name, bean);
		}
		multiSourceDataSource.setTargetDataSources(targetDataSources );
		DataSource bean = (DataSource)applicationContext.getBean("targetDataSource_"+names.get(0));
		multiSourceDataSource.setDefaultTargetDataSource(bean);
		
		return multiSourceDataSource;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext ; 
	}
	
	
	
	
	
	
}
