package com.ranger.services;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 *    消费端kong网关配置    （服务注册，路由注册，jwt，跨域cors开启）
 */
@Service
public class KongService {

    private Logger logger = Logger.getLogger(getClass());


    @Value("${ranger.kong.server}")    //空网关地址
    private String kongServer;

    @Value("${ranger.kong.name}")       //空网关注册服务名称（项目名）
    private String kongName;

    @Value("${ranger.server}")          //空网关注册的服务地址
    private String rangerServer;

    @Value("${server.port}")          //服务端口
    private String rangerPort;

    @Value("${ranger.kong.enable}")     //网关开关
    private Boolean enable;

    @Value("${ranger.kong.cors}")     //#容许跨域地址   目前为*
    private String kongCors;

    @Value("${ranger.kong.host}")     //#容许访问的host
    private String kongHost;

    @Value("${ranger.kong.web}")     //#容许访问的host
    private String kongWeb;


    private static String serviceId = "";

    public void addService(){
        try {
            HttpResponse<JsonNode> response = Unirest.put(kongServer + "/services/" + kongName)
                    .field("name", kongName)
                    .field("host", rangerServer)
                    .field("port", rangerPort)
                    .asJson();

            org.json.JSONObject body = response.getBody().getObject();
            serviceId = body.getString("id");
        } catch (UnirestException | JSONException e) {
            e.printStackTrace();
        }
    }


    public HttpResponse<JsonNode> addRoute(String apiName) {

        try {
            HttpResponse<JsonNode> response = Unirest.put(kongServer + "/routes/" + kongName + "-" + apiName)
                    .field("name",  kongName + "-" + apiName)
                    .field("hosts", kongHost)
                    .field("hosts", kongWeb)
                    .field("paths", "/" + apiName)
                    .field("service.id", serviceId)
                    .field("strip_path",false)
                    .asJson();
            if("verify".equals(apiName)){
                addJwt(response.getBody().getObject().getString("id"),"verify");
            }
            return response;

        } catch (UnirestException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void addJwt(String routeId,String apiName) {
        try {
            HttpResponse<JsonNode> response = Unirest.post(kongServer + "/routes/" + routeId + "/plugins")
                    .field("name", "jwt")
                    .asJson();
        } catch (UnirestException | JSONException e) {
            e.printStackTrace();
        }
    }


    public HttpResponse<JsonNode> addCorsConfig(String apiName) {
        if (!enable) return null;
        try {
            HttpResponse<JsonNode> response = Unirest.post(kongServer + "/services"+"/ranger-cms" + "/plugins")
                    .field("name", "cors")
                    .asJson();
            System.err.println(JSONObject.toJSONString(response));
            return response;
        } catch (UnirestException | JSONException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public void addCors(String routeId) {
        try {
            HttpResponse<JsonNode> response = Unirest.post(kongServer + "/routes/" + routeId + "/plugins")
                    .field("name", "cors")
                   // .field("config.origins", "http://testcms.acegear.com")
                    .asJson();
            System.err.println(JSONObject.toJSONString(response));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
