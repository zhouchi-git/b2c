package com.mr.bo;


import lombok.Data;

import java.util.Map;

@Data
public class PageParam {
   private  String key;
   private  Integer page=1;
   private  Integer rows=10;
   private  Map<String,Object> filters;
}
