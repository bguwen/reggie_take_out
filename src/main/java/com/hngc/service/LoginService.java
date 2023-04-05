package com.hngc.service;

import com.hngc.common.R;
import com.hngc.entity.Employee;

import java.util.Map;

public interface LoginService {
     R<Map<String,Object>> login(Employee user);

    R<String> logout();
}
