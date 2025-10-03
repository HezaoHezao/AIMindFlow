package org.aimindflow.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形实体类
 *
 * @author HezaoHezao
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeEntity<T> extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();
}