package com.hngc.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EmployeeMapperTest {
    @Autowired

    private EmployeeMapper employeeMapper;

    @Test
    void testGetPermission(){

        List<String> permission = employeeMapper.getPermission(1L);
        System.out.println(permission);


    }

}
