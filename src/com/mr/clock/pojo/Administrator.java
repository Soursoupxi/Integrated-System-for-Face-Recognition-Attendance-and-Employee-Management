//实体类——管理员类
package com.mr.clock.pojo;

public class Administrator
{
    private String username; //用户名
    private String password; //密码

    public Administrator()
    {
        super();
    }
    public Administrator(String username,String password)
    {
        super();
        this.username=username;
        this.password=password;
    }

    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username=username;
    }

    public String getPassword()
    {
        return password;
    }
    public void setPassword()
    {
        this.password=password;
    }

    @Override
    public String toString()
    {
        return "Administrator [username="+username+",password="+password+"]";
    }
}