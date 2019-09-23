package me.wuwenbin.noteblogv5.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * created by Wuwenbin on 2019-07-23 at 14:13
 *
 * @author wuwenbin
 */
@Slf4j
@Configuration
@Data
public class DataSourceConfig {

    private final Environment env;

    @Autowired
    public DataSourceConfig(Environment env) {
        this.env = env;
    }

    @Value("${db.ip:127.0.0.1}")
    private String ip;
    @Value("${db.port:3306}")
    private String port;
    @Value("${db.name:noteblogv5}")
    private String name;
    @Value("${db.username:root}")
    private String user;
    @Value("${db.password:123456}")
    private String pass;

    @Bean
    public DataSource dataSource() {
        try {
            DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
            String url = StrUtil
                    .format("jdbc:mysql://{}:{}/{}?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2B8"
                            , getIp(), getPort(), getName());
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(getUser());
            druidDataSource.setPassword(getPass());
            return druidDataSource;
        } catch (Exception e) {
            log.error("初始化数据源出错，错误信息：{}", e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
