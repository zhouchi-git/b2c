package com.mr.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JsonSpecParam {
    private  Long keyId;
    private  String[] value;
}
