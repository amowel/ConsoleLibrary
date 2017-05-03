package com.amowel.configuration;

import com.amowel.model.Author;
import com.amowel.model.Book;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;

/**
 * Created by amowel on 26.04.17.
 */
@Configuration
public class HibernateConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource()
    {
        return DataSourceBuilder.create().build();
    }
    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setAnnotatedClasses(Book.class, Author.class);
        return sessionFactoryBean;
    }
}
