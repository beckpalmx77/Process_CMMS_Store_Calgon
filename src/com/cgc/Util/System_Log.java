/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgc.Util;

import com.cgc.DB.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author beckp
 */
public class System_Log {

    public void InsTimeStamp(String SQL_TimeStamp, Connection con, PreparedStatement p) throws Exception {
        try {
            p = con.prepareStatement(SQL_TimeStamp);
            p.executeUpdate();
            p.clearParameters();
        } catch (SQLException e) {
        } finally {
        }
    }

    public void WriteTimeStamp(String SQL_TimeStamp) throws Exception {
        try {
            DBConnect objcon = new DBConnect();
            try (Connection con = objcon.openConnection_CMMS_Y()) {
                PreparedStatement p;
                p = con.prepareStatement(SQL_TimeStamp);
                p.executeUpdate();
                p.clearParameters();
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            System.out.println("Final WriteTimeStamp ");
        }
    }

    public static String LogTime(String Condition) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss");
        Timestamp cur_time = new Timestamp(new java.util.Date().getTime());
        String str_return = sdf.format(cur_time);
        return str_return;
    }

}
