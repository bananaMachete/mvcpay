package com.suda.mvcpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.suda.mvcpay.service.XaRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.text.DecimalFormat;
/**
 * 新建支付项目用于西安银行测试
 * @author xipeifeng
 * @date 2017-12-19
 */
@RestController
public class PayMainController {

    @Autowired
    private XaRestService xaRestService;

    @PostMapping(value = "/request",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String  getOrder(@RequestBody String requestJSON){
        System.out.println(requestJSON);
        JSONObject jsonMap = JSONObject.parseObject(requestJSON);
        String openpl_appsecret = "0f0531771679c85181d5f39968625014585f6875";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKuqm+0/dYALPlMX" +
                "YfN9W0EO2NjkM/X3IPk2W27bfyCNp1O3Uy08OzEcBJvU3IAxArAlajZW7FT0rWdT" +
                "VZl+lFa1uxpb7hwEmrvjtykfLhkGvKpnNFu8QUW8KdrqwdP0U7HFI8GoVBSggqqp" +
                "kI7C1W2oXTrnf0LDY8A4YblgVvAtAgMBAAECgYAJhimofADU3crTaVSx4Z6SeXRm" +
                "dCK93rwhl3ZzLBJdh/5kkqM2u0S9cMSAsMqbAh0YELtX0HLEM977AJCbN36bSaM1" +
                "XkwnQ/caP3NvZ1EqFHsjmLauoTYVQW0PqBnlONNUitEjYAJpK0i8QNmsBIdKb7i3" +
                "YIItqvUQLBiZtq85AQJBAN8037Dy37cQsSLdtlarCKWGk1ePxuj7Fw01tPAtN7VC" +
                "QMhT6UWmgUiIk+KoeJ7YbZ7DIWEieUVbvIFiBk8zXzECQQDE4zyIg/8uZLYVyFmG" +
                "ShOkt1oZVuXxE5jkTMj1rv1/hahRLpmAZ6YX6cl/zP0zzuLRVgJYWPkB7DHR+xDm" +
                "Q/m9AkAqGkUfkZCVbXacRwn/6x8kafAdEifJBqPggALzUvQxIqApqXpVAwVb8zdC" +
                "B9lIzTZQPiprsh8B8D3sgdsC0YPhAkEAkGRSXY/oUl7bxufS+BBVjaRF+HVpVz7X" +
                "Dv9dFgkKsj4Ubc0uGCRrg/gZpZdxTlB4uatJNv+xRn6xHtzwzN6nRQJBAIQTM9JG" +
                "ku7ksjFeJD4d2K0MZj/jiY/DzwQ9UOx8a7wQ9jht4wzOu8+ExlElS1jFN4B9+b/g" +
                "ZZJYwpUCxaJnTds=";
        String methodname = "scanNative";
        JSONObject jsonMap2 = (JSONObject) jsonMap.getJSONArray("tradeArray").get(0);
        DecimalFormat format = new DecimalFormat();
        String total_fee = format.format(jsonMap2.getDouble("orderMount")*100).replace(",", "");

        //重新添加数据
        JSONObject dataJson = new JSONObject();
        dataJson.put("OUT_TRADE_NO", jsonMap2.getString("orderNo"));
        dataJson.put("PAY_PASS", "000");
        dataJson.put("OPENPL_APPID", "3f921dd0cc6d60e536ca2c4ef40b0f91a584f307");
        dataJson.put("TOTAL_FEE", total_fee);
        dataJson.put("ATTATCH", "LAOT");
        dataJson.put("QRVALIDTIME", "900");
        //调用西银惠付接口
        System.out.println("payMainController sendData is ====== "+dataJson.toJSONString());
        String responseStr = xaRestService.requestOpenFlatMessage(openpl_appsecret,privateKey,methodname,dataJson.toString());
        return responseStr;
    }
}

