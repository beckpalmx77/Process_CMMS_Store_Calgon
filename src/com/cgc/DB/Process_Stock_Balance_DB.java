
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgc.DB;

import com.cgc.bean.DataBean_Transaction_Process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cgc.Util.System_Log;

/**
 * @author beck
 */
public class Process_Stock_Balance_DB {

    /**
     * @param date_from
     * @param date_to
     * @throws Exception
     */
    System_Log SysLog = new System_Log();

    public void generater_transaction_stock_process(String date_from, String date_to) throws Exception {
        ArrayList<DataBean_Transaction_Process> obj_AL_process_transaction = new ArrayList<>();
        Connection con_center_db = new DBConnect().openConnection_Center_X();
        try (Connection con = new DBConnect().openConnection_CMMS_Y()) {
            ResultSet rs;
            PreparedStatement p = null;
            PreparedStatement p1 = null;
            //Random r = new Random();
            String SQL_DEL, SQL, token, SQL_TimeStamp;
            System.out.println("Date From Param Send : " + date_from);
            System.out.println("Date To Param Send : " + date_to);

            String SQL_Update = "" ;

            SQL_DEL = " delete from t_stock_balance ; ";

            delete(SQL_DEL, con, p);

            String SQL_where = " where (to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') and to_date(format_date('" + date_to + "'),'YYYY-MM-DD')) and pgroup_id <> 'ZZ' ";
            //SQL_where = SQL_where + " and part_id = 'BA-1-001209000-CG' ";
            SQL = " select part_id,part_desc,unit_name,sum(qty_num) as qty ,(select vp.last_price from vm_part_price vp where vp.part_id = vt_transaction_stock.part_id) as price_unit "
                    + ",sum(qty_num) * (select vp.last_price from vm_part_price vp where vp.part_id = vt_transaction_stock.part_id) as total_price"
                    + ",pgroup_id,pgroup_desc "
                    + ",(select mp.pic1 from m_part mp where mp.part_id = vt_transaction_stock.part_id) as pic1 "
                    + " from vt_transaction_stock"
                    + SQL_where
                    + " Group By pgroup_id,pgroup_desc,part_id,part_desc,unit_name"
                    + " Order By pgroup_id,part_id,unit_name";

            System.out.println("SQL = " + SQL);

            token = "PR_Stock_Balance_" + new SimpleDateFormat("ddMMyy_hhmmssS").format(new Date());

            System.out.println("Token = " + token);

            SQL_TimeStamp = " Insert into t_process_log (log_id,process_id,start_time) values ('" + token + "','PR_Stock_Balance','" + new Timestamp(new java.util.Date().getTime()) + "')";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            SysLog.InsTimeStamp(SQL_TimeStamp, con, p);
            Timestamp ts = new Timestamp(new java.util.Date().getTime());
            rs = con.createStatement().executeQuery(SQL);
            int rec_cnt = 1;
            while (rs.next()) {

                String SQL_Insert = "insert into t_stock_balance "
                        + "(line_no,doc_date,part_id,part_desc,qty,price_unit,total_price,unit_name,create_date,create_by,part_group_id,part_group_name) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?)";
                System.out.println("SQL_Insert = " + SQL_Insert);
                p = con.prepareStatement(SQL_Insert);
                p.setInt(1, rec_cnt);
                p.setString(2, date_to);
                p.setString(3, rs.getString("part_id"));
                p.setString(4, rs.getString("part_desc"));
                p.setDouble(5, rs.getDouble("qty"));
                p.setDouble(6, rs.getDouble("price_unit"));
                p.setDouble(7, rs.getDouble("total_price"));
                p.setString(8, rs.getString("unit_name"));
                p.setTimestamp(9, ts);
                p.setString(10, "System");
                p.setString(11, rs.getString("pgroup_id"));
                p.setString(12, rs.getString("pgroup_desc"));
                System.out.println("part_id = " + rs.getString("part_id"));
                System.out.println("part_desc = " + rs.getString("part_desc"));
                System.out.println("qty = " + rs.getString("qty"));
                System.out.println("price_unit = " + rs.getString("price_unit"));
                System.out.println("total_price = " + rs.getString("total_price"));
                p.executeUpdate();
                rec_cnt++;

                SQL_Update = " update m_part set quantity = ?,price = ?"
                        + ",pic1 = ?"
                        + ",update_date = ? where part_id = '" + rs.getString("part_id") + "' ;";
                System.out.println("SQL_Update = " + SQL_Update);

                p1 = con_center_db.prepareStatement(SQL_Update);

                p1.setString(1, rs.getString("qty"));
                p1.setString(2, rs.getString("price_unit"));
                p1.setString(3, "http://cgc-rv016.dyndns.org:8089/CGC_UPLOADS/UPLOADS/PART/" + rs.getString("pic1"));
                p1.setTimestamp(4, ts);
                p1.executeUpdate();

            }

            System.out.println("rec_cnt = " + rec_cnt);
            SQL_TimeStamp = " Update t_process_log set condition = '" + SQL.replace("'", "#") + "', remark = '" + rec_cnt + " Record',complete_flag = 'Y' , end_time = '" + new Timestamp(new java.util.Date().getTime()) + "' where log_id = '" + token + "'";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            SysLog.InsTimeStamp(SQL_TimeStamp, con, p);
        }
    }

    private void delete(String SQL_DEL, Connection con, PreparedStatement p) throws Exception {
        try {
            System.out.println("Delete : " + SQL_DEL);
            p = con.prepareStatement(SQL_DEL);
            p.executeUpdate();
            p.clearParameters();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            System.out.println("OK");
        }
    }
}
