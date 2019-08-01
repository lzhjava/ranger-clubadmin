package com.ranger.utils;

import com.qiniu.common.Zone;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangsike on 2016/9/25.
 */
@Service
public class QiniuUtil {

    @Value("${qiniu.accessKey}")
    private String ACCESS_KEY;
    @Value("${qiniu.secretKey}")
    private String SECRET_KEY;
    @Value("${qiniu.bucket.img}")
    private String BUCKET_IMG;
    @Value("${qiniu.bucketUrl.img}")
    private String BUCKET_IMG_URL;

    @Value("${horizon.api}")
    private String apiUrl;

    private UploadManager uploadManager;

    private Auth auth;

    private BucketManager bucketManager;

    private OperationManager operationManager;

    public QiniuUtil() {
    }

    @PostConstruct
    public void init() {
        Configuration cfg = new Configuration(Zone.zone0());

        uploadManager = new UploadManager(cfg);
        auth = Auth.create(getACCESS_KEY(), getSECRET_KEY());
        bucketManager = new BucketManager(auth, cfg);
        operationManager = new OperationManager(auth, cfg);
    }

    public  Map<String, String> getUploadParams() {
        Map<String, String> params = new HashMap<>();
        params.put("token", uploadToken());
        params.put("baseUrl", getBUCKET_IMG_URL());
        return params;
    }

    public Map<String, String> getUploadParam(String key) {
        Map<String, String> params = new HashMap<>();
        params.put("token", uploadToken(key));
        params.put("baseUrl", getBUCKET_IMG_URL());
        params.put("url", getImageUrl(key));
        return params;
    }

    private String uploadToken(String key) {
        return auth.uploadToken(BUCKET_IMG, key, 3600, getPolice(key));
    }


    private String getImageUrl(String key) {
        return BUCKET_IMG_URL + key;
    }

    private String uploadToken() {
        return auth.uploadToken(BUCKET_IMG, null, 3600, getPolice(null));
    }

    private StringMap getPolice(String key) {
        if (key == null) {
            key = "$(key)";
        }
        StringMap police = new StringMap();
        police.put("callbackUrl", apiUrl + "pic/callback");
        police.put("callbackBody", "key=" + key + "&url=" + getBUCKET_IMG_URL() + key + "&mime=$(mimeType)");
        return police;
    }


    public String getACCESS_KEY() {
        return ACCESS_KEY;
    }

    public void setACCESS_KEY(String ACCESS_KEY) {
        this.ACCESS_KEY = ACCESS_KEY;
    }

    public String getSECRET_KEY() {
        return SECRET_KEY;
    }

    public void setSECRET_KEY(String SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }

    public String getBUCKET_IMG() {
        return BUCKET_IMG;
    }

    public void setBUCKET_IMG(String BUCKET_IMG) {
        this.BUCKET_IMG = BUCKET_IMG;
    }

    public String getBUCKET_IMG_URL() {
        return BUCKET_IMG_URL;
    }

    public void setBUCKET_IMG_URL(String BUCKET_IMG_URL) {
        this.BUCKET_IMG_URL = BUCKET_IMG_URL;
    }
}
