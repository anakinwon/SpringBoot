package com.pisien.springbatch.writer;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberRowMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(ResultSet rs, int i) throws SQLException {
        return new Member(rs.getInt("username")
                ,rs.getString("password")
                ,rs.getString("created_dt")
        );
    }
}
