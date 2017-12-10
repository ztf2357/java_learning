package com.beyondhost.exam.dao;

import com.beyondhost.exam.entity.RoomTypeInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

@Component
public class RoomTypeInfoDao {
    private final SqlSession sqlSession;

    public RoomTypeInfoDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void addRoomTypeInfo(RoomTypeInfo info){
        this.sqlSession.insert("addRoomTypeInfo",info);
    }
}
