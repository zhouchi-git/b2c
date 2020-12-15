package com.mr.config;

public class ApliPayConfig {

    //应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static final String APP_ID = "2021000116661278";
    //应用私钥
    public static final String MERCHANT_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCIroZrm0UbErLynCoEddcrGpMqDQcSb4o/u2DqoCHlzZnZLaxdk3sExxchtz3xJOwtuJ+8LcKMCYmxrGFHBMN3XJMHLEWc1kOq7cXspmmZPVdqPqHGKzH4J4+a9/ws+SmfYD2ZmMy2u6Bv4a52hovu8OLEm+2hI0lao2/f47jBzuV21/VYu5QKK2AfHfFWApUQFS9L4jIEvKNHjg9WJM4/68yzgrRMmkEM4jYFVS4AlYrxq0pmcgGPjJzakoqs2ATulC0hoKDj/sYXixtWKVCGfxmPSFLfLsnOagWVeAVUZonWXKi59lMkfro0HESJAS2kGqzbyd/WMSSbZIi254lJAgMBAAECggEAJjycwjG3zXEXuqNRZdoJjbdrgBIQ7mlhxZ6H6FtP4Ff6ODV8yNjfS2De6cG3hGSN5eSGW0B58m8gpCHTPOjqPdQaFaGe/9c5KvJFsoSDeikc8U4AN84RtphqpjHFzFcP+9QSp4uFiYFHysTZ57KQJxIgflwYyEF03fRm8b1lDGKI6tNum/fgROZ7RUFUvfIBhtVS+MoZpKfcWKIGWnTc6N/922/bnhJhld25760yK0Ey9TwwFtTR/+Da4zWsjpBR6bCw1UvlZXQZIcVmO1VZLLBWyFwY5BsyRQqCma+hBKmTElvkQU9PMfKtO+pIFMizcCC8jzrKPy9fIRZ8mEEmWQKBgQDb5KNOPPoGJyJQ0rbWmzfrvBeRe2el7byLmDWA3IUajSoWIMpPNhAGmcc6XTDHJSyUIVqihvHi25pxNdobQq+fhvVijLwJg6iN++Rtx71VnreF2OqONWXc4UAGg/C1M2HFq2kv1w/Lmuf7ljuaMJBvwxEJX3ZgZj4OsehVziwzLwKBgQCfIAnLObbTxoMff9Wi9stzZAlR/jT6FLB9GzEjiepxA5cy/hnJdLw4mmEbzup8IW4SV8wZaC/SAfXDJHmpBfI2Z5yiOnUgwTEuR6ClVfX05pXnUFbhghm1/f4hFHUmrq4kis72yf8Rz4vAyWS6jkVz8ll4XjPrpuLFs5CeDUNNBwKBgQCSgqpUbMhh5R02IM91+gGubap+YmzS363qjMXJUCTXZgsOp1ZkpK7U1w0pVAI2Dgy/veF3vNXZw1wJ5kmmNC5z6iYBPafj5vcdcyHmC1mZfKDm63qMugNewi44qRxufwe7/tOc7nw1usaqmJSpzYXPEXd+eT6USEs820jkMvS8PQKBgBax2BO/CH9Nybe9YLfcYQn1E4s5KU8dkkpVjX2XFvPFYlQof38HAi1Ef2Ma8MCBeBRvpzFcda2LEAUj96IOsgvmXSWaFsh0JVKHbrcF+o1SMcEK6UlCZ+5X6l3HcCnzMe0OSxRRnOpqYnE9h/s/fVJbFz2emeMbKy6tPIv+MkITAoGAKLqUTPpdXAfXgF4PHikLBvkgZoyMYcgDLfC3+SOwnyK3vgAD1ZeAgeJF3PJxMdgbLnZUZHDH6Yb7U1jBZ6PJav4q4P27lP9YQkcvTns1RafkhRiRZ5MCGQmTezrPUNNSXLr1lZqP/ZQAvVTySA0t08lKwpWGgWr262UFJ+Nx1+o=";
    //支付宝公钥
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAly/3weRrPebXg0BF9P/BlvYi0m7uht0ttvk1DbSMEL0+yc4Pj8j7+JNIGGhzVDWTxr+ew7bfsp7T5OQKN0TwnRBBDTTbhKztrAuCAg7gAWISSYua468RFUlaZMLMAoMj0XoW6VWdkEEvXNXgfj22mhY2GFoDaXiQ/Z062twSjFpGCVag/OT8QcB6TuGislCGld3PHMoClqwFB67sdYE8cQ4WfilOJ2ru7yor+qOmv8eVW9lva37oY3gYmk37PTU7aT3czr4rtltNGhBWsx2JbX5UKCBYNUqdVgr3J47cdjV44KOlajv2yj+YLUS8EXa2cYGD0vPyS8bX3iFykTkNEwIDAQAB";

    //通知的回调地址 写一个确实支付成功改订单状态等，//必须为公网地址，
    public static final String NOTIFY_URL = "http://127.0.0.1:8089/paySuccess";
    //用户支付完成回调地址//可以写内网地址
    public static final String RETURN_URL = "http://127.0.0.1:8089/callback";
    // 签名方式
    public static String SIGN_TYPE = "RSA2";
    // 字符编码格式
    public static String CHARSET = "gbk";
    // 支付宝网关
    public static String GATEWAYURL = "https://openapi.alipaydev.com/gateway.do";

}