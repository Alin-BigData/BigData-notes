package com.wangfulin.vueblog.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @projectName: vueblog
 * @description: 这里需要序列化（重要）
 * @author: Wangfulin
 * @create: 2020-06-08 13:41
 **/
@Data
public class LoginDto implements Serializable {
    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
