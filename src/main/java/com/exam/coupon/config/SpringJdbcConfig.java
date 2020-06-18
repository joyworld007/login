package com.exam.coupon.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class SpringJdbcConfig {

  @Bean
  public DataSource DataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl("jdbc:h2:file:./coupon");
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    return dataSource;
  }
}
