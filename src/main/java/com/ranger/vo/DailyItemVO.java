package com.ranger.vo;

import com.ranger.enums.FeedEnum;
import lombok.Data;

/**
 * 描述: 日报关联项目
 *
 * @author ssd
 * @create 2019-07-16 11:34 AM
 */
@Data
public class DailyItemVO {

    /**
     * 关联id
     */
    private Long relateId;

    /**
     * 关联id
     */
    private FeedEnum feedEnum;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 标题
     */
    private String title;


}
