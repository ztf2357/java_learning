package com.beyondhost.exam.dao;

import com.beyondhost.exam.entity.RoomTypeInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomTypeInfoDao {
    private final SqlSession sqlSession;

    public RoomTypeInfoDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void addRoomTypeInfos(List<RoomTypeInfo> infos){
        this.sqlSession.insert("addRoomTypeInfos",infos);
    }
}
