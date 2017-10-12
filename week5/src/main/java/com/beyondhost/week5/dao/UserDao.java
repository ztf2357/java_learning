package com.beyondhost.week5.dao;


import com.beyondhost.week5.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

@Component
public class UserDao {

    private final SqlSession sqlSession;

    public UserDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public User getUserById(long id) {
        return this.sqlSession.selectOne("getUserById", id);
    }

    public int addUserBatch(List<User> userList){
        return this.sqlSession.insert("addUserBatch", userList);
    }

    public List<User> searchUser(Map<String,Object> condition){ return this.sqlSession.selectList("searchUser",condition);}
}

