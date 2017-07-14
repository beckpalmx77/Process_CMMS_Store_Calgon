
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cgc.Util.System_Log;

/**
 * @author beck
 */
public class Process_Price_History {

    /**
     * @param date_from
     * @param date_to
     * @throws Exception
     */
    System_Log SysLog = new System_Log();

    public void generater_price_history(String date_from, String date_to) throws Exception {
        Connection con_center_db = new DBConnect().openConnection_Center_X();
        try (Connection con = new DBConnect().openConnection_CMMS_Y()) {
            ResultSet rs;
            ResultSet rs1;
            PreparedStatement p = null;
            PreparedStatement p1;
            PreparedStatement p2;
            String SQL_Insert, SQL, token, SQL_TimeStamp;

            String company_id = Mcompany(con);
            java.util.Date today = new java.util.Date();
            Timestamp ts = new java.sql.Timestamp(today.getTime());

            System.out.println("company_id = " + company_id);
            System.out.println("ts = " + ts);


            int rec_cnt = 1;
            int rec_check;

            System.out.println("Date From Param Send : " + date_from);
            System.out.println("Date To Param Send : " + date_to);

            //String SQL_where = " where (to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') and to_date(format_date('" + date_to + "'),'YYYY-MM-DD')) and pgroup_id <> 'ZZ' ";

            SQL = " select * from vt_part_price_history ";

            System.out.println("SQL = " + SQL);

            token = "PR_Stock_Balance_" + new SimpleDateFormat("ddMMyy_hhmmssS").format(new Date());


            System.out.println("Token = " + token);

            SQL_TimeStamp = " Insert into t_process_log (log_id,process_id,start_time) values ('" + token + "','PR_Stock_Balance','" + new Timestamp(new java.util.Date().getTime()) + "')";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            SysLog.InsTimeStamp(SQL_TimeStamp, con, p);
            rs = con.createStatement().executeQuery(SQL);
            System.out.println("executeQuery = " + SQL);
            while (rs.next()) {

                rec_check = 0;

                System.out.println("vt_part_price_history = "
                        + rs.getString("part_id_compare") + " : "
                        + rs.getString("name_t")
                        + " : " + rs.getString("avl_purchase_name") + " : "
                        + rs.getString("price_purchase") + " : "
                        + rs.getString("date_purchase"));

                SQL = " select * from t_tran_part_price_history where part_id = '" + rs.getString("part_id_compare") + "'"
                        + " and date_purchase = '" + rs.getString("date_purchase") + "'"
                        + " and avl_purchase = '" + rs.getString("avl_purchase") + "'"
                        + " and company_id = '" + company_id + "'"
                        + " and doc_id = '" + rs.getString("doc_id") + "'";
                rs1 = con.createStatement().executeQuery(SQL);
                while (rs1.next()) {
                    rec_check = 1;
                }

                System.out.println("Out Loop rec_check = " + rec_check);
                if (rec_check == 0) {
                    System.out.println("Insert Data" + rs.getString("part_id_compare"));
                    SQL_Insert = " Insert into t_tran_part_price_history (doc_id,date_purchase,part_id,part_name,avl_purchase,avl_purchase_name,price_purchase,create_date,create_by,company_id) values (?,?,?,?,?,?,?,?,?,?) ;";
                    p1 = con.prepareStatement(SQL_Insert);
                    p1.setString(1, rs.getString("doc_id"));
                    p1.setString(2, rs.getString("date_purchase"));
                    p1.setString(3, rs.getString("part_id_compare"));
                    p1.setString(4, rs.getString("name_t"));
                    p1.setString(5, rs.getString("avl_purchase"));
                    p1.setString(6, rs.getString("avl_purchase_name"));
                    p1.setString(7, rs.getString("price_purchase"));
                    p1.setTimestamp(8, ts);
                    p1.setString(9, "System");
                    p1.setString(10, company_id);
                    p1.executeUpdate();

                    p2 = con_center_db.prepareStatement(SQL_Insert);
                    p2.setString(1, rs.getString("doc_id"));
                    p2.setString(2, rs.getString("date_purchase"));
                    p2.setString(3, rs.getString("part_id_compare"));
                    p2.setString(4, rs.getString("name_t"));
                    p2.setString(5, rs.getString("avl_purchase"));
                    p2.setString(6, rs.getString("avl_purchase_name"));
                    p2.setString(7, rs.getString("price_purchase"));
                    p2.setTimestamp(8, ts);
                    p2.setString(9, "System");
                    p2.setString(10, company_id);
                    p2.executeUpdate();
                }
            }

            SQL_TimeStamp = " Update t_process_log set condition = '" + SQL.replace("'", "#") + "', remark = '" + rec_cnt + " Record',complete_flag = 'Y' , end_time = '" + new Timestamp(new java.util.Date().getTime()) + "' where log_id = '" + token + "'";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            SysLog.InsTimeStamp(SQL_TimeStamp, con, p);
        }
    }

    private static String Mcompany(Connection con) throws Exception {
        String str_return = "";
        ResultSet rs;
        try {
            rs = con.createStatement().executeQuery("select company_id,name_t from mcompany where delete_flag = 'N' and doc_type = 'Y'");
            while (rs.next()) {
                str_return = rs.getString("company_id");
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            System.out.println(str_return);
        }

        return str_return;

    }

}
