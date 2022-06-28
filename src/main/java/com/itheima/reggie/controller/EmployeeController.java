package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     *
     * @param request  存session
     * @param employee Employee
     * @return R<Employee>
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

//        接收密码
        String password = employee.getPassword();
//        进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//         注入查询查询条件
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());

        Employee emp = employeeService.getOne(lambdaQueryWrapper);
//        账号比对
        if (emp == null) {
            return R.error("账号错误登录失败！");
        }
//          密码比对
        if (!(emp.getPassword().equals(password))) {
            return R.error("密码错误登录失败！");
        }
//          状态比对
        if (emp.getStatus() == 0) {
            return R.error("该账号已被禁用！");
        }
//      登陆成功
//      将id存入session
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出功能
     *
     * @param request request
     * @return R
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    /**
     * 新增员工
     *
     * @param request  request
     * @param employee employee
     * @return R
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工信息：{}", employee);
//      设置默认密码：123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));

//        设置创建时间
//        employee.setCreateTime(LocalDateTime.now());
//        设置修改时间
//        employee.setUpdateTime(LocalDateTime.now());
//        获取当前登录用户id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        设置创建人id
//        employee.setCreateUser(empId);
//        设置修改人id
//        employee.setUpdateUser(empId);
//        保存
        employeeService.save(employee);


        return R.success("新增员工成功！");
    }

    /**
     * 分页查询
     * @param page page
     * @param pageSize pageSize
     * @param name name
     * @return R
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);

//        构造分页构造器
        Page<Employee> pageInfo = new Page(page, pageSize);
//        构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
//        添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
//        添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
//        执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 员工账号信息更新
     *
     * @param request  request
     * @param employee employee
     * @return R
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("-------------------" + employee.toString() + "-------------------");
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateTime(LocalDateTime.now());
//        employeeService.updateById(employee);
        return R.success("账号信息更新成功！");
    }

    /**
     * 根据id查询员工信息
     *
     * @param id id
     * @return R
     */
    @GetMapping({"/{id}"})
    public R<Employee> getById(@PathVariable Long id) {

        log.info("根据id查询员工信息！{}",id);

        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息！");
    }

}

