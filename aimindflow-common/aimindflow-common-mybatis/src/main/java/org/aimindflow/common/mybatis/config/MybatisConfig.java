package org.aimindflow.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.aimindflow.common.mybatis.handler.DataScopeHandler;
import org.aimindflow.common.mybatis.handler.impl.DefaultDataScopeHandler;
import org.aimindflow.common.mybatis.interceptor.DataScopeInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis配置类
 *
 * @author HezaoHezao
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("org.aimindflow.common.mybatis")
@MapperScan("org.aimindflow.**.mapper")
public class MybatisConfig {

    /**
     * MyBatis-Plus拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 数据权限插件
        interceptor.addInnerInterceptor(new DataScopeInterceptor(dataScopeHandler()));
        return interceptor;
    }

    /**
     * 数据权限处理器
     */
    @Bean
    @ConditionalOnMissingBean(DataScopeHandler.class)
    public DataScopeHandler dataScopeHandler() {
        return new DefaultDataScopeHandler();
    }
}