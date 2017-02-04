package cn.lexy.auth;

import cn.lexy.auth.mapper.utils.PagingSupportPlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by john on 16/8/2.
 */
@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ComponentScan
@EnableTransactionManagement
@MapperScan("cn.lexy.auth.mapper")
public class Application {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new org.apache.tomcat.jdbc.pool.DataSource();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:**/mapper/*.xml"));

        // start config mybatis plugins
        PagingSupportPlugin pagePlugin = new PagingSupportPlugin();
        pagePlugin.setDatabaseType(PagingSupportPlugin.Dialect.mysql.name());
        pagePlugin.setPageSize(10);
        pagePlugin.setIdentifier("page");
        pagePlugin.setPattern(".*list.*");
        Interceptor[] mybatisPlugins = new Interceptor[1];
        mybatisPlugins[0] = pagePlugin;
        sqlSessionFactoryBean.setPlugins(mybatisPlugins);

        return sqlSessionFactoryBean.getObject();
    }


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
