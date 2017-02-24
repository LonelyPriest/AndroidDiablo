package com.diablo.dt.diablo.utils;

import com.diablo.dt.diablo.entity.AuthenShop;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.Retailer;

import java.util.List;

/**
 * Created by buxianhui on 17/2/24.
 */

public class DiabloUtils {
    private static DiabloUtils mInstance;

    public static DiabloUtils getInstance() {
        if (null == mInstance){
            mInstance = new DiabloUtils();
        }

        return mInstance;
    }

    private DiabloUtils() {

    }

    public AuthenShop getShop(List<AuthenShop> shops, Integer index){
        AuthenShop shop = null;
        for ( Integer i = 0; i < shops.size(); i++){
            if (index.equals(shops.get(i).getShop())){
                shop = shops.get(i);
                break;
            }
        }

        return shop;
    }

    public Employee getEmployeeByNumber(List<Employee> employees, String number){
        Employee employee = null;
        for ( Integer i = 0; i < employees.size(); i++){
            if (number.equals(employees.get(i).getNumber())){
                employee = employees.get(i);
                break;
            }
        }
        return employee;
    }

    public Retailer getRetailer(List<Retailer> retailers, Integer index){
        Retailer r = null;
        for (Integer i = 0; i < retailers.size(); i++){
            if ( index.equals(retailers.get(i).getId())){
                r = retailers.get(i);
                break;
            }
        }

        return r;
    }
}
