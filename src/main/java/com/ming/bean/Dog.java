package com.ming.bean;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class Dog  {
    @Column
    private String id;
    private String name;
    private  Integer age;

    public Dog(String id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dog)) return false;
        Dog dog = (Dog) o;
        return Objects.equals(id, dog.id) &&
                Objects.equals(getName(), dog.getName()) &&
                Objects.equals(getAge(), dog.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), getAge());
    }
}
