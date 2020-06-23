package com.wangfulin.features.java8;

import com.wangfulin.features.common.Employee;

/**
 * @projectName: newfuturesbaseon8ormore
 * @description: 通过薪水过滤
 * @author: Wangfulin
 * @create: 2020-06-10 21:38
 **/
public class FilterEmployeeForSalary implements MyPredicate<Employee> {
    @Override
    public boolean test(Employee employee) {
        return employee.getSalary() > 5000;
    }
}
