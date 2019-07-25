package com.ranger.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.question.contract.QuestionsAndAnswersContract;
import com.ranger.question.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:问答
 *
 * @author: sunshuaidong
 * @create: 2018-12-06 18:08
 * @brief:
 */
@RestController
@RequestMapping("/exemption/questions")
public class QAController {

    @Reference(interfaceClass = QuestionsAndAnswersContract.class, timeout = 1200000)
    private QuestionsAndAnswersContract QAContract;

    /**
     * 俱乐部问答获取可展示的问答列表
     * @return
     */
    @GetMapping("")
    public ResultVO selectAllQuestions(){
        return QAContract.selectAllQuestions();
    }



}
