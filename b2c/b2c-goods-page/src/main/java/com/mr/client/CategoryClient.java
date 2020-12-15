package com.mr.client;


import com.mr.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")

public interface CategoryClient extends CategoryApi {

}
