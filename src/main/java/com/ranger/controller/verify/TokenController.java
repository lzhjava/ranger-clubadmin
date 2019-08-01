package com.ranger.controller.verify;

import com.ranger.club.vo.ResultVO;
import com.ranger.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**  七牛token获取
 * @author huipeng
 * @Title: TokenController
 * @ProjectName ranger-clubadmin
 * @Description: TODO
 * @date 2019/8/1下午2:28
 */
@RestController
@RequestMapping("/verify/token")
public class TokenController {

    @Autowired
    QiniuUtil qiniuUtil;



    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResultVO createUploadToken() {
        return new ResultVO<>(qiniuUtil.getUploadParams());
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.POST)
    public Object createUploadToken(@PathVariable String key) {
        return new ResultVO<>(qiniuUtil.getUploadParam(key));
    }
}
