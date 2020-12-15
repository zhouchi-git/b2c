package com.mr.client;



import com.mr.api.SpecParamApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("item-service")
public interface SpecClient extends SpecParamApi {

}
