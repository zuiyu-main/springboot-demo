package com.tz.mybatisplus.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

//import mybatis.mate.annotation.Algorithm;
//import mybatis.mate.annotation.FieldEncrypt;

/**
 * <p>
 * 当前系统用户
 * </p>
 *
 * @author tz
 * @since 2019-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="User对象", description="当前系统用户")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    private String userName;

    //    @FieldEncrypt(algorithm = Algorithm.RSA)
    private String fullName;

    private String password;

    private String salt;
    @TableLogic
    private Integer deleted;


}
