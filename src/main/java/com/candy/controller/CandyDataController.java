package com.candy.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.candy.bean.CandyDataBean;
import com.candy.utils.CandyHttpRequestUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@Controller
public class CandyDataController {

    private static final String candyUrl = "http://www.dianrui.com/api/adsSearchDataApi.php";

    /**
     * 调用api获取数据
     */
    @GetMapping("/candy_data")
    @ResponseBody
    public JSONObject getCandyData() throws IOException {
        String username = "travel@qq.com";
        Integer userid = 27635;
        Integer planid = 12911;
        String token = DigestUtils.md5Hex((username+userid).getBytes());
        StringBuilder url = new StringBuilder(candyUrl);
        url.append("?").append("username=").append(username).append("&token=").append(token).append("&planid=").append(planid);
        String response = CandyHttpRequestUtils.getHttpResposeStrData(url.toString());
        /*CandyDataBean candyInfo = JSON.parseObject(response,CandyDataBean.class);
        System.out.print(candyInfo.toString());*/
        return JSONObject.parseObject(response);
    }
}
