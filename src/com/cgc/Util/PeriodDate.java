package com.cgc.Util;

import com.cgc.DB.DBConnect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeriodDate {

    public String Start_Period(String doc_type) throws Exception {
        String SQL = " select * from mperiod where doc_type = '" + doc_type + "'";
        String Return_Value = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs ;
        try {
            rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                Return_Value = rs.getString("start_period");
            }
        } finally {
            try {
                //rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return Return_Value;

    }

    public String End_Period(String doc_type) throws Exception {
        String SQL = " select * from mperiod where doc_type = '" + doc_type + "'";
        String Return_Value = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs ;
        try {
            rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                Return_Value = rs.getString("end_period");
            }
        } finally {
            try {
                //rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return Return_Value;

    }

    public String Start_Year(String doc_type) throws Exception {
        String SQL = " select * from mperiod where doc_type = '" + doc_type + "'";
        String Return_Value = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs ;
        try {
            rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                Return_Value = rs.getString("start_year");
            }
        } finally {
            try {
                //rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return Return_Value;

    }

    public String End_Year(String doc_type) throws Exception {
        String SQL = " select * from mperiod where doc_type = '" + doc_type + "'";
        String Return_Value = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs ;
        try {
            rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                Return_Value = rs.getString("end_year");
            }
        } finally {
            try {
                //rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return Return_Value;

    }

    public String Start_Current_Month(String month) throws Exception {
        String SQL = " select * from mmonth where month_id = '" + month + "'";
        String Return_Value = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs ;

        String language;
        String country;
        String dialog_heading;
        String dialog_message;

        language = System.getProperty("user.language");
        country = System.getProperty("user.country");
        System.out.println("language = " + language);
        System.out.println("country = " + country);
        switch (language) {
            case "th":
                dialog_heading = "SimplestI18N ไทย";
                dialog_message = "ยินดีต้อนรับสู่ SimplestI18N ภาคภาษาไทย";
                break;
            case "en":
                dialog_heading = "SimplestI18N English";
                dialog_message = "Welcome to SimplestI18N, English version.";
                break;
            default:
                dialog_heading = "SimplestI18N";
                dialog_message = "Welcome to SimplestI18N.";
                break;
        }

        System.out.println("dialog_heading = " + dialog_heading);
        System.out.println("dialog_message = " + dialog_message);

        try {
            rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                Return_Value = rs.getString("start_date");
            }
        } finally {
            try {
                //rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return Return_Value;

    }

    public String End_Current_Month(String month) throws Exception {
        String SQL = " select * from mmonth where month_id = '" + month + "'";
        String Return_Value = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs ;
        try {
            rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                Return_Value = rs.getString("end_date");
            }
        } finally {
            try {
                //rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return Return_Value;

    }

}
