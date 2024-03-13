package com.myrpc.context;


public class NacosProperties extends RpcRegisterProperties {



    public static final String ADDR_PREFIX="nacos://";

    public static final String SERVER_ADDR_KEY="serverAddr";


    public static final String AUTH_USERNAME_KEY="user";
    public static final String AUTH_PASSWORD_KEY="password";



    boolean authEnabled;

    String userName;
    String password;

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
