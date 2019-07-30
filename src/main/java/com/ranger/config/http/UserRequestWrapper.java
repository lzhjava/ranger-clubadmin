package com.ranger.config.http;

import com.ranger.enums.RedisKeyEnum;
import com.ranger.utils.RedisStringUtil;
import com.ranger.utils.Shift;
import com.ranger.utils.UserCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Vector;

public class UserRequestWrapper extends HttpServletRequestWrapper {

    private RedisStringUtil redisUtil;

    public UserRequestWrapper(HttpServletRequest request, RedisStringUtil redisUtil) {
        super(request);
        this.redisUtil = redisUtil;
    }

    @Override
    public String getHeader(String name) {
        return super.getHeader(name);
    }

    @Override
    public long getDateHeader(String name) {
        return super.getDateHeader(name);
    }

    @Override
    public int getIntHeader(String name) {
        return super.getIntHeader(name);
    }

    private final static String USERID = "userId";
    private final static String XCONSUMERUSERNAME = "X-Consumer-Username";

    @Override
    public Enumeration<String> getHeaders(String name) {
        Vector<String> vector = new Vector<>();
        if (XCONSUMERUSERNAME.equals(name)) {
            String userTokenId = super.getHeader(XCONSUMERUSERNAME);
            if (userTokenId != null) {
                String userId = redisUtil.get(RedisKeyEnum.USERTOKEN + userTokenId);
                if (userId != null) {
                    vector.add(userId);
                } else {
                    vector.add("0");
                }
            } else {
                vector.add("0");
            }
            return vector.elements();
        }
        if (USERID.equals(name)) {
            String userTokenId = super.getHeader(XCONSUMERUSERNAME);
            if (userTokenId != null) {
                String userId = redisUtil.get(RedisKeyEnum.USERTOKEN + userTokenId);
                if (userId != null) {
                    vector.add(userId);
                } else {
                    Shift.fatal(UserCode.USER_NOT_LOGIN);
                }
            } else {
                Shift.fatal(UserCode.USER_NOT_LOGIN);
            }
            return vector.elements();
        }
        return super.getHeaders(name);
    }
}
