package ua.foxminded.university.config;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ua.foxminded.university.dao")
@EnableAutoConfiguration(exclude = { HibernateJpaAutoConfiguration.class })
@PropertySource("classpath:hibernate.properties")
@ComponentScan("ua.foxminded.university")
@Slf4j
public class AppContext {

    @Autowired
    private Environment environment;
    
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        log.info("Return new PersistenceExceptionTranslationPostProcessor()");
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
                tomcat.enableNaming();
                log.info("Enable JNDI naming to tomcat");
                return super.getTomcatWebServer(tomcat);
            }
            @Override
            protected void postProcessContext(org.apache.catalina.Context context) {
                ContextResource resource = new ContextResource();
                resource.setType("org.apache.tomcat.jdbc.pool.DataSource");
                resource.setName("jdbc/postgres");
                resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
                resource.setProperty("driverClassName", "org.postgresql.Driver");
                resource.setProperty("url", "jdbc:postgresql://127.0.0.1:5432/university");
                resource.setProperty("username", "admin");
                resource.setProperty("password", "1234");
                              
                context.getNamingResources().addResource(resource);
            }
        };
    }
    
    @Bean
    public DataSource dataSource() throws NamingException {
        return (DataSource) new InitialContext().lookup(environment.getProperty("jdbc.url"));
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() throws NamingException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "ua.foxminded.university.service.entities" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        hibernateProperties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        hibernateProperties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        hibernateProperties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return hibernateProperties;
    }


    @Bean
    public PlatformTransactionManager hibernateTransactionManager() throws NamingException {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
