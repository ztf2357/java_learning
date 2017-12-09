package com.beyondhost.exam.dao;

import com.beyondhost.exam.entity.OrgInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrgInfoDao {

    private final SqlSession sqlSession;

    public OrgInfoDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void addOrgInfo(OrgInfo info){
        this.sqlSession.insert("addOrgInfo",info);
    }

}
