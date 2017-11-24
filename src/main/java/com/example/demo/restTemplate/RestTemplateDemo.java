package com.example.demo.restTemplate;

import com.example.demo.domains.ResultAssociatedWithToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by DengChuan on 2017/10/22.
 */
@Component
public class RestTemplateDemo {
    public static ResultAssociatedWithToken getJsonResponse(String token) {
        RestTemplate restTemplate = new RestTemplate();


//        System.out.println("https://reactome.org/AnalysisService/token/"+token);
        return restTemplate.getForObject("https://reactome.org/AnalysisService/token/{token}?pageSize=50&page=1&sortBy=ENTITIES_PVALUE&order=ASC&resource=TOTAL", ResultAssociatedWithToken.class, token);
//        return restTemplate.getForObject("https://reactome.org/AnalysisService/token/{token}?sortBy=ENTITIES_PVALUE&order=ASC&resource=TOTAL", ResultAssociatedWithToken.class, token);
    }
}
