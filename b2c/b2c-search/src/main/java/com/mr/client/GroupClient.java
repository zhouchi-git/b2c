package com.mr.client;

import com.mr.api.GroupApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GroupClient extends GroupApi {
}
