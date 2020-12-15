package com.mr.order.client;

import com.mr.order.bo.AddressBo;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserClient {
    public  AddressBo getAddressById(Long id){
        AddressBo addressBo = new AddressBo();
        Map<Long, AddressBo> addressMap = addressBo.addressMap;
        return  addressMap.get(id);
    }

}
