package com.ranger.init;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.ranger.services.KongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import springfox.documentation.swagger.schema.ApiModelTypeNameProvider;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * 描述:
 *
 * @author chaiwei
 * @create 2018-11-27 下午2:31
 */
@Component
public class InitBean {

    @Autowired
    private KongService kongService;

    @PostConstruct
    public void initKongApi() {
        try {
            kongService.addService();
            List<String> apis = Arrays.asList("verify", "exemption");

            apis.forEach(apiName -> {
                HttpResponse<JsonNode> response = kongService.addRoute(apiName);
                if (!ObjectUtils.nullSafeEquals(null,response)){
                    HttpResponse<JsonNode> jsonNodeHttpResponse1 = kongService.addCorsConfig(apiName);
                    if (!ObjectUtils.nullSafeEquals(null,jsonNodeHttpResponse1)){
                        kongService.addCors(response.getBody().getObject().getString("id"));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
