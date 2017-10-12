package com.beyondhost.week5.controller;

import com.alibaba.fastjson.JSON;
import com.beyondhost.week5.dao.UserDao;
import com.beyondhost.week5.domain.User;
import com.beyondhost.week5.util.RandomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user/")
public class UserController {

    private final UserDao _userDao;

    @Autowired
    public UserController(UserDao userDao) {
       this._userDao = userDao;
    }

    @GetMapping(value = "{id}")
    public String getUserById(@PathVariable(value="id") long id) {
        User result = _userDao.getUserById(id);
        if(result==null)return null;
        return JSON.toJSONString(result);
    }

    /**
     * 动态构造User并批量插入
     * @param count  构造的记录数量
     * @return
     */
    @PostMapping(value="addbatch")
    public String addUserBatch(@RequestParam("count") int count)
    {
        List<User> userList = new ArrayList<User>();
        for(int i=0;i<count;i++)
        {
            User user = new User();
            user.Name = RandomInfo.getEnglishName();
            user.FullName = RandomInfo.getChineseName();
            user.Age = RandomInfo.getNum(50,100);
            user.Gender = RandomInfo.getNum(0,1);
            user.Mobile = RandomInfo.getTel();

            user.Email = RandomInfo.getEmail(10,20);
            user.Address = RandomInfo.getRandomRoad();
            userList.add(user);
        }
        int rows =  _userDao.addUserBatch(userList);
        return String.format("%d rows has been inserted",rows);
    }

    /**
     * 根据多个条件查询用户
     * @param name 用户名
     * @param fullName 全名
     * @param startAge 起始年龄
     * @param endAge 结束年龄
     * @param mobile 电话
     * @return
     */
    @GetMapping(value = "search",produces="application/json;charset=UTF-8")
    public String searchUser(@RequestParam(value="name",required = true) String name,
                             @RequestParam(value="fullName",required = false) String fullName,
                             @RequestParam(value="startAge",required = false) int startAge,
                             @RequestParam(value="endAge",required = false) int endAge,
                             @RequestParam(value="mobile",required = false) String mobile) {
        Map<String,Object> condition = new HashMap<String,Object>();
        if(name!=null && name.length()>0)
        {
            condition.put("Name",name);
        }
        if(fullName!=null && fullName.length()>0)
        {
            condition.put("FullName",fullName);
        }
        if(startAge>0)
        {
            condition.put("StartAge",startAge);
        }
        if(endAge>0)
        {
            condition.put("EndAge",endAge);
        }
        if(mobile!=null && mobile.length()>0)
        {
            condition.put("Mobile",mobile);
        }
        return JSON.toJSONString(_userDao.searchUser(condition));
    }
}
