package xyz.trieye.assignment.deal.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DB {
    @Value("classpath:crypto.db")
    private String dbPath;

    @Bean
    public DataSource dataSource(ResourceLoader resourceLoader) {
        try {
            String path = resourceLoader.getResource(dbPath).getURI().getPath();
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl("jdbc:sqlite:" + path);
            dataSource.setDriverClassName("org.sqlite.JDBC");
            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database", e);
        }
    }
}
