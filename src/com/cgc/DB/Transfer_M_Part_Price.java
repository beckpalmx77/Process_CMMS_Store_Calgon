/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import com.cgc.Util.System_Log;
import com.cgc.Util.UtiDatabase;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Beckpalmx No7
 */
public class Transfer_M_Part_Price {

    private final UtiDatabase objuti = new UtiDatabase();

    public void generater_transaction_trasnfer_process(String date_from, String date_to, String process_id, String table, String doc_type, String r, String username) throws Exception {
        try (Connection con = new DBConnect().openConnection_CMMS_Y()) {
            try (Connection con1 = new DBConnect().openConnection_CMMS_Y()) {
                Process_transactionDB Process_Trans = new Process_transactionDB();
                System_Log SysLog = new System_Log();
                ResultSet rs;
                PreparedStatement p_timestamp = null;
                PreparedStatement p, p1;

                String SQL_MAIN, SQL, token, SQL_TimeStamp;

                String currentYear = date_to.substring(6, 10);
                //String currentYear = "2559";

                int count_loop = 0;

                String previousYear = Integer.toString((Integer.parseInt(currentYear) - 1));

                System.out.println("currentYear = " + currentYear);
                System.out.println("previousYear = " + previousYear);

                System.out.println("M_Part Transfer Start Process process");

                token = "PR_Trans_Part_Price_" + new SimpleDateFormat("ddMMyy_hhmmssS").format(new Date());
                SQL_TimeStamp = " Insert into t_process_log (log_id,process_id,start_time) values ('" + token + "','PR_Trans_Part_Price','" + new Timestamp(new java.util.Date().getTime()) + "')";
                System.out.println("Insert TimeStamp SQL = " + SQL_TimeStamp);
                SysLog.InsTimeStamp(SQL_TimeStamp, con, p_timestamp);

                SQL = " select * from " + table + " where price_year = '" + previousYear + "' and delete_flag <> 'Y' order by runno ";
                System.out.println("First SQL = " + SQL);
                p = con.prepareStatement(SQL);
                rs = p.executeQuery();

                while (rs.next()) {

                    SQL_MAIN = "select count(part_id) as num from " + table
                            + " where part_id = '" + rs.getString("part_id") + "'"
                            + " and price_year = '" + currentYear + "' and delete_flag <> 'Y' ";

                    System.out.println("SQL_MAIN = " + SQL_MAIN);

                    if (objuti.numRowdatabase(SQL_MAIN) == 0) {
                        count_loop++;
                        //System.out.println("count_loop = " + count_loop);
                        SQL_MAIN = "INSERT INTO " + table
                                + " (part_id,price_year,part_group_id"
                                + ",price_month_1,price_month_2,price_month_3"
                                + ",price_month_4,price_month_5,price_month_6"
                                + ",price_month_7,price_month_8,price_month_9"
                                + ",price_month_10,price_month_11,price_month_12"
                                + ",create_by,create_date) "
                                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                        System.out.println("Insert = " + SQL_MAIN);
                        p1 = con1.prepareStatement(SQL_MAIN);
                        p1.setString(1, rs.getString("part_id"));
                        p1.setString(2, currentYear);
                        p1.setString(3, rs.getString("part_group_id"));
                        p1.setString(4, rs.getString("price_month_1"));
                        p1.setString(5, rs.getString("price_month_2"));
                        p1.setString(6, rs.getString("price_month_3"));
                        p1.setString(7, rs.getString("price_month_4"));
                        p1.setString(8, rs.getString("price_month_5"));
                        p1.setString(9, rs.getString("price_month_6"));
                        p1.setString(10, rs.getString("price_month_7"));
                        p1.setString(11, rs.getString("price_month_8"));
                        p1.setString(12, rs.getString("price_month_9"));
                        p1.setString(13, rs.getString("price_month_10"));
                        p1.setString(14, rs.getString("price_month_11"));
                        p1.setString(15, rs.getString("price_month_12"));
                        p1.setString(16, username);
                        p1.setTimestamp(17, new Timestamp(new java.util.Date().getTime()));
                        p1.executeUpdate();

                    } else {
                        System.out.println("Check Dup Part ID");
                    }

                }

                SQL_TimeStamp = " Update t_process_log set condition = '" + SQL.replace("'", "#") + "', remark = '" + count_loop + " Record',complete_flag = 'Y' , end_time = '" + new Timestamp(new java.util.Date().getTime()) + "' where log_id = '" + token + "'";
                System.out.println("Update TimeStamp SQL = " + SQL_TimeStamp);
                SysLog.InsTimeStamp(SQL_TimeStamp, con, p_timestamp);
                System.out.println("End Process process ");
            } finally {
                System.out.println("OK");
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException sqlee) {
                    System.out.println("Info : " + sqlee);
                }
            }
        }
    }
}
