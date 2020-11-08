package com.tz.redismanager.test

import com.tz.redismanager.domain.Person

/**
 * <p></p>
 * @time 2019-01-26 22:44
 * @version 1.0
 * */
class hello2 {

    def helloWithoutParam(){
        println "start to call helloWithoutParam!"
        return "success, helloWithoutParam";
    }

    def helloWithParam(Person person, id){
        System.out.println(person);
        person.setName("update");
        println "start to call helloWithParam, param{person:" + person + ", id:" + id + "}";
        return "success, helloWithParam";
    }
}
