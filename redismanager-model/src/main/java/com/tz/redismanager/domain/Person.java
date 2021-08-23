package com.tz.redismanager.domain;

/**
 * person对象
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2019-01-30 23:27
 **/
public class Person {
    private String name;
    private int age;
    private Person child;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person getChild() {
        return child;
    }

    public void setChild(Person child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", child=" + child +
                '}';
    }
}
