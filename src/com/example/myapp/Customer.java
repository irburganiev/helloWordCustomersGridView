package com.example.myapp;

import java.io.Serializable;

/**
 * Created by ilnur on 01.03.2015.
 */
public class Customer implements Serializable {
    private String _name;

    private String _surName;

    private Integer _age;

    public Integer getId() {
        return Id;
    }

    private final Integer Id;


    public Customer(String _name, String _surName, Integer _age, Integer id) {
        this._name = _name;
        this._surName = _surName;
        this._age = _age;
        Id = id;
    }

    public void Update(Customer customer)
    {
        set_age(customer.get_age());
        set_name(customer.get_name());
        set_surName(customer.get_surName());
    }


    @Override
    public String toString() {
        return "Customer Name:" + get_name() + " Age:" + get_age();
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_surName() {
        return _surName;
    }

    public void set_surName(String _surName) {
        this._surName = _surName;
    }

    public Integer get_age() {
        return _age;
    }

    public void set_age(Integer _age) {
        this._age = _age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (!Id.equals(customer.Id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Id.hashCode();
        return result;
    }
}

