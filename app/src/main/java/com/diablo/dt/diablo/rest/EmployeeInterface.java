package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by buxianhui on 17/2/24.
 */

public interface EmployeeInterface {
    @GET("list_employe")
    Call<List<Employee>> listEmployee(@Header("cookie") String token);
}
