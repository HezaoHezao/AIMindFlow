package org.aimindflow.common.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础服务接口：直接继承 MyBatis-Plus 的 IService
 * 避免重复声明导致的方法签名冲突问题。
 */
public interface IBaseService<T> extends IService<T> {
}