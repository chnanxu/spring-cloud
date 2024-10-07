package com.chen.utils.result;//package com.chen.utils.result;
//
//
//import com.alibaba.fastjson.JSON;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//
//import java.util.Date;
//
//
//public class JwtUtils{
//
//
//    private static final String secret="111111";
//
//    /**生成JWT token */
//    public static String token(Authentication authentication){
//        return JWT.create()
//                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
//                .withAudience(JSON.toJSONString(authentication))
//                .sign(Algorithm.HMAC256(secret));
//    }
//    /**根据指定日期返回token
//     * @param authentication 认证信息
//     * @param time 过期时间 单位是毫秒
//     * @return 返回token*/
//    public static String token(Authentication authentication,Long time){
//        return JWT.create()
//                .withExpiresAt(new Date(System.currentTimeMillis()+ 1000L * 60 * 60 * 24 * 30))
//                .withAudience(JSON.toJSONString(authentication))
//                .sign(Algorithm.HMAC256(secret));
//    }
//
//    /** 验证token合法性 */
//    public static void tokenVerify(String token){
//        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(secret)).build();
//        jwtVerifier.verify(token);
//        JWT.decode(token).getExpiresAt();
//    }
//
//
//
//}
