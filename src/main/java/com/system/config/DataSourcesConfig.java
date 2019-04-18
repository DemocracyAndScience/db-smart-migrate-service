package com.system.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.system.config.support.MultiSourceDataSource;
import com.system.config.support.MyDataSource;
import com.system.utils.constants.SystemConstants;
import com.system.utils.constants.en.BeanNameConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Order(3)
@Configuration
public class DataSourcesConfig implements ApplicationContextAware {

    @Autowired
    private DatabasesNames databasesNames;

    @Autowired
    private DataSourceInfos dataSourceInfos;


    private ApplicationContext applicationContext;

    /* *//**
     * 两个数据源设置
     **//*
    @Bean(name = "sourceDataSource_shuangshi")
    @ConfigurationProperties(prefix = "sync_source_shuangshi.datasource")
    @Qualifier("sourceDataSource_shuangshi")
    public DataSource sourceDataSource() {
        DataSourceBuilder create = DataSourceBuilder.create();
        return create.build();
    }

    @Bean(name = "targetDataSource_shuangshi")
    @ConfigurationProperties(prefix = "sync_target_shuangshi.datasource")
    @Qualifier("targetDataSource_shuangshi")
    public DataSource targetDataSource() {
        return DataSourceBuilder.create().build();
    }

     *//**
     * 两个数据源设置
     **//*
    @Bean(name = "sourceDataSource_shuangshi-assistant")
    @ConfigurationProperties(prefix = "sync_source_shuangshi-assistant.datasource")
    @Qualifier("sourceDataSource_shuangshi-assistant")
    public DataSource sourceDataSource_shuangshi_assistant() {
        DataSourceBuilder create = DataSourceBuilder.create();
        return create.build();
    }

    @Bean(name = "targetDataSource_shuangshi-assistant")
    @ConfigurationProperties(prefix = "sync_target_shuangshi-assistant.datasource")
    @Qualifier("targetDataSource_shuangshi-assistant")
    public DataSource targetDataSource_shuangshi_assistant() {
        DataSource build = DataSourceBuilder.create().build();
        return build;
    }



    */

    /**
     * 两个数据源设置
     **//*
    @Bean(name = "sourceDataSource_xxl-job")
    @ConfigurationProperties(prefix = "sync_source_xxl-job.datasource")
    @Qualifier("sourceDataSource_xxl-job")
    public DataSource sourceDataSource_xxl_job() {
        DataSource build = DataSourceBuilder.create().build();
        return build;
    }

    @Bean(name = "targetDataSource_xxl-job")
    @ConfigurationProperties(prefix = "sync_target_xxl-job.datasource")
    @Qualifier("targetDataSource_xxl-job")
    public DataSource targetDataSource_xxl_job() {
        return DataSourceBuilder.create().build();
    }
*/
    @Bean
    @Qualifier("sourceDataSource")
    public MultiSourceDataSource sourceMultiSourceDataSource() {
        MultiSourceDataSource multiSourceDataSource = new MultiSourceDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        List<String> names = databasesNames.getNames();
        for (String name : names) {
            DataSource bean = (DataSource) applicationContext.getBean(BeanNameConstants.SOURCE_DATASOURCE_ + name);
            targetDataSources.put(name, bean);
        }
        multiSourceDataSource.setTargetDataSources(targetDataSources);
        DataSource bean = (DataSource) applicationContext.getBean(BeanNameConstants.SOURCE_DATASOURCE_ + names.get(0));
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
            DataSource bean = (DataSource) applicationContext.getBean(BeanNameConstants.TARGET_DATASOURCE_ + name);
            targetDataSources.put(name, bean);
        }
        multiSourceDataSource.setTargetDataSources(targetDataSources);
        DataSource bean = (DataSource) applicationContext.getBean(BeanNameConstants.TARGET_DATASOURCE_ + names.get(0));
        multiSourceDataSource.setDefaultTargetDataSource(bean);

        return multiSourceDataSource;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        Map<String, Map<String, Map<String, MyDataSource>>> nameSpaces = dataSourceInfos.getNameSpaces();
        Set<Map.Entry<String, Map<String, Map<String, MyDataSource>>>> entries = nameSpaces.entrySet();
        for (Map.Entry<String, Map<String, Map<String, MyDataSource>>> entry :
                entries) {
            String schemaName = entry.getKey();
            Map<String, Map<String, MyDataSource>> values = entry.getValue();
            Set<Map.Entry<String, Map<String, MyDataSource>>> entries1 = values.entrySet();
            for (Map.Entry<String, Map<String, MyDataSource>> entry1 :
                    entries1) {
                String typeName = entry1.getKey();
                Map<String, MyDataSource> value = entry1.getValue();
                Set<Map.Entry<String, MyDataSource>> entries2 = value.entrySet();
                for (Map.Entry<String, MyDataSource> entry2 : entries2) {
                    MyDataSource value1 = entry2.getValue();
                    if (SystemConstants.SOURCE.equals(typeName)) {
                        BeanDefinitionBuilder beanDefinitionBuilder = getBeanDefinitionBuilder(value1);
                        beanFactory.registerBeanDefinition(BeanNameConstants.SOURCE_DATASOURCE_ + schemaName, beanDefinitionBuilder.getBeanDefinition());
                    } else if (SystemConstants.TARGET.equals(typeName)) {
                        BeanDefinitionBuilder beanDefinitionBuilder = getBeanDefinitionBuilder(value1);
                        beanFactory.registerBeanDefinition(BeanNameConstants.TARGET_DATASOURCE_ + schemaName, beanDefinitionBuilder.getBeanDefinition());
                    }

                }
            }
        }
    }

    public BeanDefinitionBuilder getBeanDefinitionBuilder(MyDataSource value1) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(DruidDataSource.class);
        beanDefinitionBuilder.addPropertyValue("driverClassName", value1.getDriverClassName());
        beanDefinitionBuilder.addPropertyValue("url", value1.getUrl());
        beanDefinitionBuilder.addPropertyValue("username", value1.getUsername());
        beanDefinitionBuilder.addPropertyValue("password", value1.getPassword());
        return beanDefinitionBuilder;
    }
}
