package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.club.contract.ClubContract;
import com.ranger.club.contract.ClubManageContract;
import com.ranger.club.enums.ClubJoinVerifyType;
import com.ranger.club.enums.ClubMemberType;
import com.ranger.photo.contract.PhotoContract;
import com.ranger.photo.po.PhotoPO;
import com.ranger.photo.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

/**   俱乐部相册管理
 * @author huipeng
 * @Title: ClubController
 * @ProjectName ranger-clubadmin
 * @Description: TODO
 * @date 2019/7/25下午2:42
 */
@RestController
@RequestMapping("/verify/album")
public class PhotoController {


    @Reference(interfaceClass = PhotoContract.class, timeout = 1200000)
    private  PhotoContract photoContract;


    /**   创建相册
     * @param relevanceId   关联id
     * @param userId   创建者ID
     * @param name     相册名称
     * @param type     相册类型
     * @return
     */
    @PostMapping("/{relevanceId}")
    public ResultVO createAlbm(@PathVariable Long relevanceId ,
                               @RequestParam(value = "userId", required = false,defaultValue = "0")  Long userId ,
                               @RequestParam String name,
                               @RequestParam (defaultValue = "1" ) Integer type){
       return photoContract.createAlbm(relevanceId, userId, name, type);
    }


    /**   相册名称修改 （目前用户身份未做效验）
     * @param albumId   相册ID
     * @param userId    修改用户id
     * @param relevanceId   关联id
     * @param name      名字
     * @return
     */
    @PutMapping("/{albumId}")
    public  ResultVO modifyAlbumName(@PathVariable Long albumId,
                                     @RequestParam(value = "userId", required = false,defaultValue = "0") Long userId,
                                     @RequestParam Long relevanceId,
                                     @RequestParam String name){
        return photoContract.modifyAlbumName(albumId, userId, relevanceId, name);

    }


    /**   俱乐部相册删除  （目前用户身份未做效验）
     * @param albumId   相册Id
     * @param userId    用户id
     * @return
     */
    @DeleteMapping("/{albumId}")
    public  ResultVO delAlbum(@PathVariable Long albumId,
                              @RequestParam(value = "userId", required = false,defaultValue = "0") Long userId){
        return  photoContract.delAlbum(albumId, userId);
    }


    /**   俱乐部后台查询该类型所有相册
     * @param relevanceId   关联id
     * @param type    类型
     * @param pageNo   页码
     * @param pageSize   条数
     * @return
     */
    @GetMapping("/{relevanceId}")
    public ResultVO searchAllAlbum(@PathVariable Long relevanceId,
                                   @RequestParam (defaultValue = "1" )Integer type,
                                   @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                   @RequestParam(required = false, defaultValue = "10")Integer pageSize){
        return  photoContract.searchAllAlbum(relevanceId,type,pageNo,pageSize);
    }


    /**   查询该相册所有照片
     * @param albumId   相册Id
     * @param pageNo    页码
     * @param pageSize    条数
     * @return
     */
    @GetMapping("/photo/{albumId}")
    public  ResultVO searchAlbumAllPhoto(@PathVariable Long albumId,
                                         @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                         @RequestParam(required = false, defaultValue = "10")Integer pageSize){
        return  photoContract.searchAlbumAllPhoto(albumId, pageNo, pageSize);
    }



    /**       相册添加照片
     * @param relevanceId   关联id
     * @param albumId    相册id
     * @param photoPO     照片交互po
     * @return
     */
    @PostMapping("/photo/{relevanceId}/{albumId}")
    public ResultVO addPhoto(@PathVariable Long relevanceId,@PathVariable Long albumId,
                             @RequestParam(value = "userId", required = false,defaultValue = "0") Long userId,
                             @RequestBody PhotoPO photoPO){
                photoPO.setUserId(userId);
        return  photoContract.addPhoto(relevanceId, albumId, photoPO);
    }


    /**    删除照片
     * @param photoId   照片id
     * @param albumId   相册id
     * @param userId    用户id
     * @return
     */
    @DeleteMapping("/photo/{albumId}/{photoId}")
    public ResultVO delPhoto(@PathVariable Long photoId,@PathVariable Long albumId,
                             @RequestParam(value = "userId", required = false,defaultValue = "0") Long userId){
        return  photoContract.delPhoto(photoId, albumId, userId);
    }

}
