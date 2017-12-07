package com.beyondhost.exam.dao;

import com.beyondhost.exam.entity.OrgInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

@Component
public class OrgInfoDao {
    private final SqlSession sqlSession;

    public OrgInfoDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public OrgInfo getOrgInfoByPoi(long poiId) {
        return this.sqlSession.selectOne("getOrgInfoByPoiId", poiId);
    }

}
