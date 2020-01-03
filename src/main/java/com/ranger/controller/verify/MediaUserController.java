package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.user.contract.UserInfoApi;
import com.ranger.user.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**   媒体用户接口服务
 * @author huipeng
 * @Title: UserInfoController
 * @ProjectName ranger-app
 * @Description: TODO
 * @date 2018/11/27下午3:03
 */
@RestController
@RequestMapping("/verify/mediaUser")
public class MediaUserController {

    @Reference(interfaceClass = UserInfoApi.class, timeout = 1200000)
    private UserInfoApi userInfoApi;


    /**    媒体组查询
     * @param pageNo
     * @param pageSize
     * @param nickname    昵称
     * @param whitelist    是否设置白名单
     * @return
     */
    @GetMapping("")
    public ResultVO mediaUserPage(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                  @RequestParam(required = false, defaultValue = "15")Integer pageSize,
                                   String nickname,
                                   Boolean whitelist,
                                   Long id,
                                   Boolean recommend ){
        return userInfoApi.mediaUserPage(pageNo, pageSize, nickname, whitelist,id,recommend);
    }


    /**    俱乐部获取已关注的媒体用户列表
     * @param clubId
     * @return
     */
    @GetMapping("/{clubId}")
    public ResultVO getClubMediaUser(@PathVariable Long clubId,
                                     @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                     @RequestParam(required = false, defaultValue = "15")Integer pageSize){
        return userInfoApi.getClubMediaUser(clubId,pageNo,pageSize);

    }


    /** 俱乐部设置媒体用户，修改媒体用户
     * @param clubId
     * @param mediaUserId
     * @return
     */
    @PostMapping("/{clubId}")
    public ResultVO saveClubMediaUser(@PathVariable Long clubId, @RequestBody List<Long> mediaUserId){
        return userInfoApi.saveClubMediaUser(clubId,mediaUserId);

    }

    /** 俱乐部取关媒体用户
     * @param clubId
     * @param mediaUserId
     * @return
     */
    @DeleteMapping("/callOff/{clubId}")
    public ResultVO delClubMediaUser(@PathVariable Long clubId,@RequestBody List<Long> mediaUserId){
        return  userInfoApi.delClubMediaUser(clubId, mediaUserId);
    }



}
