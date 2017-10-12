package com.beyondhost.week5.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message="用户Id不能为空")
    public Long Id;
    @NotNull(message="用户名不能为空")
    public String Name;
    public String FullName;
    public int Gender;

    @Min(value = 50,message = "年龄不得小于50")
    @Max(value = 100,message = "年龄不得大于100")
    public int Age;
    public String Mobile;
    @NotNull
    @Email(message ="邮箱格式非法！")
    public String Email;

    @NotNull
    @Size(min = 3, max = 200)
    public String Address;

}
