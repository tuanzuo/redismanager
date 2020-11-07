package com.tz.redismanager.test;

import com.tz.redismanager.util.DateUtils;
import com.tz.redismanager.util.UUIDUtils;
import io.jsonwebtoken.*;
import org.joda.time.DateTime;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>JWT测试</p>
 * https://jwt.io/
 * https://github.com/jwtk/jjwt
 * @version 1.5.0
 * @time 2020-11-01 22:05
 **/
public class JwtTest {

    public static void main(String[] args) throws InterruptedException {
        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(UUIDUtils.generateId().getBytes());
        System.err.println(base64EncodedSecretKey);
        Map<String, Object> claims = new HashMap<>();
        claims.put("name","tuanzuo");
        claims.put("age",18);
        String token = Jwts.builder()
                .addClaims(claims)
                .setExpiration(new DateTime().plusSeconds(5).toDate())
                .signWith(SignatureAlgorithm.HS512, base64EncodedSecretKey)
                .compact();
        System.out.println(token);
        Claims clm = Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody();
        System.out.println(clm.get("name"));
        System.out.println(clm.get("age"));
        System.out.println("过期时间:"+DateUtils.dateToStr(clm.getExpiration(),DateUtils.YYYY_MM_DD_HH_MM_SS));
        System.out.println("当前时间:"+DateUtils.dateToStr(new Date(),DateUtils.YYYY_MM_DD_HH_MM_SS));

        System.err.println("---------------");

        TimeUnit.SECONDS.sleep(6);
        try {
            clm = Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody();
            System.out.println(clm.get("name"));
            System.out.println(clm.get("age"));
            System.out.println("过期时间:"+DateUtils.dateToStr(clm.getExpiration(),DateUtils.YYYY_MM_DD_HH_MM_SS));
            System.out.println("当前时间:"+DateUtils.dateToStr(new Date(),DateUtils.YYYY_MM_DD_HH_MM_SS));
        } catch (ExpiredJwtException e) {
            System.err.println("token已过期");
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
