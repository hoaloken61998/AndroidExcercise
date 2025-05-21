package com.example.connectors;

import com.example.models.Employee;
import com.example.models.ListEmployee;

public class EmployeeConnectors {
    public Employee login(String usr, String pwd)
    {
        ListEmployee le = new ListEmployee();
        le.gen_dataset();

        for (Employee emp : le.getEmployees())
        {
            if (emp.getUsername().equalsIgnoreCase(usr) && emp.getPassword().equals(pwd))
            {
                return emp;
            }

        }
        return null;
    }
}
