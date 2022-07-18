package ua.foxminded.university.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ComponentScan("ua.foxminded.university.dao")
@ComponentScan("ua.foxminded.university.service")
@PropertySources({
@PropertySource("classpath:datasource.properties"),
@PropertySource("classpath:hibernateH2.properties")
})
@EnableTransactionManagement
@Configuration
public class HibernateConfigTest {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Bean
    public LocalSessionFactoryBean getSessionFactory() throws NamingException, SQLException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());        
        sessionFactory.setPackagesToScan(new String[] {"ua.foxminded.university.service.entities"});
        sessionFactory.setHibernateProperties(getHibernateH2Properties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() throws NamingException, SQLException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource dataSource = (DataSource) envContext.lookup("datasource/ds");
        return dataSource;
    }
    
    private Properties getHibernateH2Properties() {
        Properties hibernateH2Properties = new Properties();
        hibernateH2Properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateH2Properties.put("hibernate.hbm2ddl.auto", "create-drop");
        hibernateH2Properties.put("hibernate.show_sql", "true");
        return hibernateH2Properties;
    }
   
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }
}
