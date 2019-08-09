package com.ranger.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.ranger.club.contract.ClubContract;
import com.ranger.club.dto.ClubDTO;
import com.ranger.club.enums.ClubMemberType;
import com.ranger.push.contract.MessagePushContract;
import com.ranger.user.contract.UserAuthApi;
import com.ranger.user.vo.ResultVO;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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


    @Reference(interfaceClass = ClubContract.class, timeout = 1200000)
    private ClubContract clubContract;

    @Reference(interfaceClass = MessagePushContract.class, timeout = 1200000)
    private MessagePushContract messagePushContract;


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

        System.out.println("——————————————————进入俱乐部后台服务————————————————");

        ResultVO resultVO = userAuthApi.phoneLogin(phone, password, platform);

        Object body = JSONObject.parseObject(JSONObject.toJSONString(resultVO)).get("body");
        if (ObjectUtils.nullSafeEquals(null,body)){
            return resultVO.USER_RIGHTS_ERROR;
        }
        Object userId = JSONObject.parseObject(JSONObject.toJSONString(body)).get("userId");
        List<ClubDTO> clubDTOS = clubContract.searchClubsByUserId(Long.valueOf(String.valueOf(userId)), ClubMemberType.FOUNDER);
        if (ObjectUtils.nullSafeEquals(0,clubDTOS.size())){
            return ResultVO.USER_HAVENOT_MANAGE_CLUB;
        }
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("body",body);
        maps.put("clubs",clubDTOS);
        return  new ResultVO<>("",0,maps);
    }




    /**
     * 发送短信
     * @param mobile :手机号
     * @return
     */
    @PostMapping("/sms")
    public com.ranger.push.vo.ResultVO sendSMS (@RequestParam(value = "mobile", required = true) String mobile){
        return messagePushContract.returnCode(mobile);
    }




    /**   手机短信修改密码
     * @param phone   手机号
     * @param code    验证码
     * @return
     */
    @PostMapping("/sms/password")
    public ResultVO smsLogin(@RequestParam(value = "phone", required = true)String phone,
                             @RequestParam(value = "code", required = true)String code,
                             @RequestParam(value = "password", required = true)String password)  {
        if (messagePushContract.msgCheck(phone, code)) {
            return userAuthApi.resetPassword(phone,password);
        } else {
            return ResultVO.VERIFY_CODE_ERROR;
        }
    }





}
