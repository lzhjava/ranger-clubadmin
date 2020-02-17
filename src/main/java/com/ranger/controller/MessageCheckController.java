package com.ranger.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.statistics.contract.CodeContract;
import com.ranger.statistics.vo.ResultVO;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @author ssd
 * @create 2019-08-13 3:26 PM
 */
@RestController
@RequestMapping("/exemption/msgCheck")
public class MessageCheckController {
    @Reference(interfaceClass = CodeContract.class, timeout = 1200000)
    private CodeContract codeContract;

    /**    微信敏感信息验证
     * @param type   小程序type
     * @param content   效验内容
     * @return
     */
    @PostMapping("")
    public ResultVO msgCheck(@RequestParam Integer type, @RequestParam String content){
        if (ObjectUtils.nullSafeEquals(null,content) || ObjectUtils.nullSafeEquals("",content)){
            return new ResultVO(true);
        }
        return new ResultVO( codeContract.check(type,content));
    }
}
