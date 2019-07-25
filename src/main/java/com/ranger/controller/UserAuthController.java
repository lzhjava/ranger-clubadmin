package com.ranger.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.user.contract.UserAuthApi;
import com.ranger.user.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

/**    用户登陆注册接口服务
 * @author huipeng
 * @Title: UserController
 * @ProjectName ranger-app
 * @Description: TODO   提供用户接口常规服务    包括登陆注册等
 * @date 2018/11/27下午2:30
 */
@RestController
@RequestMapping("/exemption/userAuth")
public class UserAuthController {

    @Reference(interfaceClass = UserAuthApi.class, timeout = 1200000)
    private UserAuthApi userAuthApi;


    /**    手机号密码登陆
     * @param phone   手机号码
     * @param password     密码
     * @param platform    平台
     * @return
     */
    @PostMapping("/phoneLogin")
    public ResultVO phoneLogin(@RequestParam(value = "phone", required = true)String phone,
                               @RequestParam(value = "password", required = true)String password,
                               @RequestParam(required = false, defaultValue = "2")  Integer platform){
       return userAuthApi.phoneLogin(phone,password,platform);
    }



    /**
     * 重置密码
     */
    @PostMapping("/password")
    public ResultVO resetPassword(@RequestParam(value = "userPhone", required = true)String userPhone,
                                  @RequestParam(value = "password", required = true)String password){
        return  userAuthApi.resetPassword(userPhone,password);
    }



}
