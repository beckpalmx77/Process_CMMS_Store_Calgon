
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

import com.cgc.Util.DateUtil;

/**
 * @author beck
 */
public class Process_transactionDB {

    private static String Find_Unit(String SQL, Connection con) throws Exception {
        ResultSet rs = null;
        String Unit_ID = "";
        try {
            rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                Unit_ID = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return Unit_ID;
    }

    private static int numrow(String SQL, Connection con) throws Exception {
        ResultSet rs = null;
        int numrow = 0;
        try {
            rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                numrow = rs.getInt(1);
            }
        } catch (SQLException e) {
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return numrow;
    }

    /**
     * @param date_from
     * @param date_to
     * @throws Exception
     */
    public void generater_transaction_process(String date_from, String date_to) throws Exception {
        ArrayList<DataBean_Transaction_Process> obj_AL_process_transaction = new ArrayList<>();
        //Connection con = new DBConnect().openConnection_CMMS_Y();
        try (Connection con = new DBConnect().openConnection_CMMS_Y()) {
            ResultSet rs;
            PreparedStatement p = null;
            //Random r = new Random();
            String SQL_DEL, SQL, SQL1, token, SQL_TimeStamp;
            int Record, count_loop = 0;
            System.out.println("Date From Param Send : " + date_from);
            System.out.println("Date To Param Send : " + date_to);

            SQL_DEL = " delete from t_transaction_stock ; ALTER SEQUENCE seq_t_transaction_stock RESTART WITH 1; ";
            //+ " where process_id in ('PR_001') ";
            //+ " and to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') AND to_date(format_date('" + date_to + "'),'YYYY-MM-DD')";

            delete(SQL_DEL, con, p);

            SQL = " select runno,doc_id,doc_type,part_id,price_unit,qty,unit_id,wh_id,location_id,doc_date_chk as doc_date from vd_adjust_stock_detail "
                    + " where doc_type in ('R','W','B','D') "
                    + " and to_date(format_date(doc_date_chk),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') AND to_date(format_date('" + date_to + "'),'YYYY-MM-DD')";

            SQL1 = " select count(*) from vd_adjust_stock_detail "
                    + " where doc_type in ('R','W','B','D') "
                    + " and to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') AND to_date(format_date('" + date_to + "'),'YYYY-MM-DD')";

            System.out.println("SQL = " + SQL);
            //Record = numrow(SQL1, con);
            //System.out.println("PR_001 Record = " + Record);
            token = "PR_001_" + new SimpleDateFormat("ddMMyy_hhmmssS").format(new Date());
            System.out.println("Token = " + token);

            SQL_TimeStamp = " Insert into t_process_log (log_id,process_id,start_time) values ('" + token + "','PR_001','" + new Timestamp(new java.util.Date().getTime()) + "')";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            InsTimeStamp(SQL_TimeStamp, con, p);

            //if (Record >= 1) {
            rs = con.createStatement().executeQuery(SQL);
            DataBean_Transaction_Process bean = new DataBean_Transaction_Process();
            while (rs.next()) {
                bean.setProcess_id("PR_001");
                bean.setDoc_id(rs.getString("doc_id"));
                //bean.setDoc_date(rs.getString("doc_date"));
                bean.setDoc_date(rs.getString("doc_date") == null ? "-" : rs.getString("doc_date"));
                String doc_type;
                if (rs.getString("doc_type").equalsIgnoreCase("B") || rs.getString("doc_type").equalsIgnoreCase("R")) {
                    //bean.setDoc_type("+");
                    doc_type = "+";
                } else {
                    //bean.setDoc_type("-");
                    doc_type = "-";
                }

                bean.setDoc_type(doc_type);
                bean.setPart_id(rs.getString("part_id") == null ? "" : rs.getString("part_id"));
                bean.setPrice_unit((rs.getString("price_unit") == null || rs.getString("price_unit").equals("")) ? "0" : rs.getString("price_unit"));
                bean.setQty((rs.getString("qty") == null || rs.getString("qty").equals("")) ? "0" : rs.getString("qty"));

                //String Unit_Id = "-";
                String Unit_Id = Find_Unit("select mpu.unit_id from vm_part mpu where mpu.part_id = '" + rs.getString("part_id") + "'", con);

                bean.setUnit_id(rs.getString("unit_id") == null ? Unit_Id : rs.getString("unit_id"));

                //bean.setWh_id(rs.getString("wh_id").equals("")?"-":rs.getString("wh_id"));
                bean.setWh_id("001");
                //bean.setLocation_id(rs.getString("location_id").equals("")?"-":rs.getString("location_id"));
                bean.setLocation_id("-");

                //System.out.println("process_id 001 part_id = " + rs.getString("part_id") + " doc_type = " + doc_type);
                obj_AL_process_transaction.add(bean);

                if (doc_type.equals("+")) {
                    Update_LastPriceUnit(con, rs.getString("part_id"), rs.getString("doc_date"), rs.getString("price_unit"));
                }

                Update_LastPartMovement(con, rs.getString("part_id"), rs.getString("doc_date"));

                count_loop++;
                insert(obj_AL_process_transaction, con, p);
            }
            //}
            System.out.println("P1 count_loop = " + count_loop);
            SQL_TimeStamp = " Update t_process_log set condition = '" + SQL.replace("'", "#") + "', remark = '" + count_loop + " Record',complete_flag = 'Y' , end_time = '" + new Timestamp(new java.util.Date().getTime()) + "' where log_id = '" + token + "'";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            InsTimeStamp(SQL_TimeStamp, con, p);
        }
    }

    /**
     * @param date_from
     * @param date_to
     * @throws Exception
     */
    public void generater_transaction_process2(String date_from, String date_to) throws Exception {
        ArrayList<DataBean_Transaction_Process> obj_AL_process_transaction = new ArrayList<>();
        //Connection con = new DBConnect().openConnection_CMMS_Y();
        try (Connection con = new DBConnect().openConnection_CMMS_Y()) {
            ResultSet rs;
            PreparedStatement p = null;
            //Random r = new Random();
            String SQL_DEL, SQL_DEL1, SQL, SQL1, token, SQL_TimeStamp;
            String doc_date;
            int count_loop = 0;
            //int Record = 0;
            System.out.println("Date From Param Send 2 : " + date_from);
            System.out.println("Date To Param Send 2 : " + date_to);

            SQL_DEL = " delete from t_transaction_stock "
                    + " where process_id in ('PR_002') ";
            //+ " and to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') AND to_date(format_date('" + date_to + "'),'YYYY-MM-DD')";

            //SQL_DEL1 = " delete from t_transaction_stock_error "
            //+ " where process_id in ('PR_002') ";
            //+ " and to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') AND to_date(format_date('" + date_to + "'),'YYYY-MM-DD')";
            //SQL = " select runno,doc_id,line_no,doc_date,ap_date1,ap_date2,doc_type,part_id,price_unit,to_char(qty_number,'999999.99') as qty,unit_id from vd_stock_withdraw_detail_store_transaction_process "
            SQL = " select runno,doc_id,line_no,doc_date,ap_date1,ap_date2,doc_type,part_id,price_unit,to_char(qty_number,'999999.99') as qty,unit_id from vd_stock_withdraw_detail_store_trans "
                    //+ " where withdraw_for = 'N' and complete_flag = 'Y' "
                    + " where withdraw_for = 'N' and qty_number > 0 "
                    + " and (part_id IS NOT NULL and unit_id IS NOT NULL) and to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') AND to_date(format_date('" + date_to + "'),'YYYY-MM-DD')";

            //SQL1 = " select count(*) from vd_stock_withdraw_detail_store_transaction_process "
            SQL1 = " select count(*) from vd_stock_withdraw_detail_store_trans "
                    //+ " where withdraw_for = 'N' and complete_flag = 'Y' "
                    + " where withdraw_for = 'N' and qty_number > 0 "
                    + " and (part_id IS NOT NULL and unit_id IS NOT NULL) and to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') AND to_date(format_date('" + date_to + "'),'YYYY-MM-DD')";

            System.out.println("SQL 2 = " + SQL);
            //Record = numrow(SQL1, con);
            //Record = 0;

            //System.out.println("PR_002 Record = " + Record);
            token = "PR_002_" + new SimpleDateFormat("ddMMyy_hhmmssS").format(new Date());
            System.out.println("Token = " + token);

            SQL_TimeStamp = " Insert into t_process_log (log_id,process_id,start_time) values ('" + token + "','PR_002','" + new Timestamp(new java.util.Date().getTime()) + "')";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            InsTimeStamp(SQL_TimeStamp, con, p);

            //if (Record >= 1) {
            //delete(SQL_DEL, con, p);
            //delete(SQL_DEL1, con, p);
            System.out.println("Process 002 SQL = " + SQL);
            rs = con.createStatement().executeQuery(SQL);

            DataBean_Transaction_Process bean = new DataBean_Transaction_Process();
            while (rs.next()) {
                bean.setProcess_id("PR_002");
                bean.setDoc_id(rs.getString("doc_id"));
                bean.setLine_no(rs.getString("line_no") == null ? "-" : rs.getString("line_no"));

                if (rs.getString("ap_date1").equals("-") && rs.getString("ap_date2").equals("-")) {
                    doc_date = rs.getString("doc_date");
                    System.out.println("Doc_Date Doc_Date = " + doc_date);
                } else if (rs.getString("ap_date1").equals("-") && !rs.getString("ap_date2").equals("-")) {
                    doc_date = rs.getString("ap_date2");
                    System.out.println("Doc_Date ap_date2 = " + doc_date);
                } else {
                    doc_date = rs.getString("ap_date1");
                    System.out.println("Doc_Date ap_date1 = " + doc_date);
                }

                bean.setDoc_date(doc_date);

                String doc_type;

                if (rs.getString("doc_type").equalsIgnoreCase("R")) {
                    //bean.setDoc_type("+");
                    doc_type = "+";
                } else {
                    //bean.setDoc_type("-");
                    doc_type = "-";
                }
                bean.setDoc_type(doc_type);
                bean.setPart_id(rs.getString("part_id") == null ? "-" : rs.getString("part_id"));
                bean.setPrice_unit((rs.getString("price_unit") == null || rs.getString("price_unit").equals("")) ? "0" : rs.getString("price_unit"));
                bean.setQty((rs.getString("qty") == null || rs.getString("qty").equals("")) ? "0" : rs.getString("qty"));

                //String Unit_Id = "-";
                String Unit_Id = Find_Unit("select mpu.unit_id from vm_part mpu where mpu.part_id = '" + rs.getString("part_id") + "'", con);
                bean.setUnit_id(rs.getString("unit_id") == null ? Unit_Id : rs.getString("unit_id"));

                //bean.setUnit_id(rs.getString("unit_id") == null ? "-" : rs.getString("unit_id"));
                bean.setWh_id("001");
                bean.setLocation_id("-");

                //System.out.println("process_id 002 part_id = " + rs.getString("part_id") + " doc_type = " + doc_type);
                obj_AL_process_transaction.add(bean);

                if (doc_type.equals("+")) {
                    Update_LastPriceUnit(con, rs.getString("part_id"), rs.getString("doc_date"), rs.getString("price_unit"));
                }

                Update_LastPartMovement(con, rs.getString("part_id"), rs.getString("doc_date"));

                if (rs.getString("part_id") == null || rs.getString("doc_date") == null || rs.getString("doc_type") == null || rs.getString("unit_id") == null) {
                    //insert_error(obj_AL_process_transaction, con, p);
                    System.out.println("Error Can't Insert Data");
                } else {
                    insert(obj_AL_process_transaction, con, p);
                }

                count_loop++;

            }
            System.out.println("P2 count_loop : " + count_loop);
            SQL_TimeStamp = " Update t_process_log set condition = '" + SQL.replace("'", "#") + "', remark = '" + count_loop + " Record',complete_flag = 'Y' , end_time = '" + new Timestamp(new java.util.Date().getTime()) + "' where log_id = '" + token + "'";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            InsTimeStamp(SQL_TimeStamp, con, p);
        }
    }

    /**
     * @param date_from
     * @param date_to
     * @throws Exception
     */
    public void generater_transaction_process_oil_withdraw(String date_from, String date_to) throws Exception {
        ArrayList<DataBean_Transaction_Process> obj_AL_process_transaction = new ArrayList<>();
        //Connection con = new DBConnect().openConnection_CMMS_Y();
        try (Connection con = new DBConnect().openConnection_CMMS_Y()) {
            ResultSet rs;
            PreparedStatement p = null;
            //Random r = new Random();
            String SQL_DEL, SQL_DEL1, SQL, SQL1, token, SQL_TimeStamp;
            int count_loop = 0;
            //int Record = 0;
            System.out.println("Date From Param Send 3 : " + date_from);
            System.out.println("Date To Param Send 3 : " + date_to);

            SQL_DEL = " delete from t_transaction_stock "
                    + " where process_id in ('PR_003') ";

            SQL = " select runno,doc_id,doc_date,part_id,to_char(qty_number,'999999.99') as qty,unit_id_use "
                    + " from vd_oil_withdraw "
                    + " where (part_id IS NOT NULL and unit_id_use IS NOT NULL) and to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') AND to_date(format_date('" + date_to + "'),'YYYY-MM-DD')";

            SQL1 = " select count(*) from vd_oil_withdraw "
                    + " where (part_id IS NOT NULL and unit_id_use IS NOT NULL) and to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + date_from + "'),'YYYY-MM-DD') AND to_date(format_date('" + date_to + "'),'YYYY-MM-DD')";

            System.out.println("SQL 3 = " + SQL);

            token = "PR_003_" + new SimpleDateFormat("ddMMyy_hhmmssS").format(new Date());
            System.out.println("Token = " + token);

            SQL_TimeStamp = " Insert into t_process_log (log_id,process_id,start_time) values ('" + token + "','PR_003','" + new Timestamp(new java.util.Date().getTime()) + "')";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            InsTimeStamp(SQL_TimeStamp, con, p);

            //if (Record >= 1) {
            //delete(SQL_DEL, con, p);
            //delete(SQL_DEL1, con, p);
            System.out.println("Process 003 SQL = " + SQL);
            rs = con.createStatement().executeQuery(SQL);

            DataBean_Transaction_Process bean = new DataBean_Transaction_Process();
            while (rs.next()) {
                bean.setProcess_id("PR_003");
                bean.setDoc_id(rs.getString("doc_id"));
                bean.setDoc_date(rs.getString("doc_date") == null ? "-" : rs.getString("doc_date"));

                bean.setDoc_type("-");
                bean.setPart_id(rs.getString("part_id") == null ? "-" : rs.getString("part_id"));
                //bean.setPrice_unit((rs.getString("price_unit") == null || rs.getString("price_unit").equals("")) ? "0" : rs.getString("price_unit"));
                bean.setQty((rs.getString("qty") == null || rs.getString("qty").equals("")) ? "0" : rs.getString("qty"));

                //String Unit_Id = "-";
                String Unit_Id = Find_Unit("select mpu.unit_id from vm_part mpu where mpu.part_id = '" + rs.getString("part_id") + "'", con);
                bean.setUnit_id(rs.getString("unit_id_use") == null ? Unit_Id : rs.getString("unit_id_use"));

                //bean.setUnit_id(rs.getString("unit_id") == null ? "-" : rs.getString("unit_id"));
                bean.setWh_id("001");
                bean.setLocation_id("-");

                //System.out.println("process_id 002 part_id = " + rs.getString("part_id") + " doc_type = " + doc_type);
                obj_AL_process_transaction.add(bean);
                Update_LastPartMovement(con, rs.getString("part_id"), rs.getString("doc_date"));

                if (rs.getString("part_id") == null || rs.getString("doc_date") == null || rs.getString("unit_id_use") == null) {
                    //insert_error(obj_AL_process_transaction, con, p);
                    System.out.println("Error Can't Insert Data");
                } else {
                    insert(obj_AL_process_transaction, con, p);
                }

                count_loop++;

            }
            System.out.println("P3 count_loop : " + count_loop);
            SQL_TimeStamp = " Update t_process_log set condition = '" + SQL.replace("'", "#") + "', remark = '" + count_loop + " Record',complete_flag = 'Y' , end_time = '" + new Timestamp(new java.util.Date().getTime()) + "' where log_id = '" + token + "'";
            System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
            InsTimeStamp(SQL_TimeStamp, con, p);
        }
    }

    private void insert(ArrayList<DataBean_Transaction_Process> obj_AL, Connection con, PreparedStatement p) throws Exception {

        int i;
        try {
            p = con.prepareStatement("insert into t_transaction_stock "
                    + "(process_id,doc_id,line_no,doc_date,doc_type,part_id,price_unit,qty,unit_id,wh_id,location_id,create_date,create_by) "
                    + "values"
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            for (DataBean_Transaction_Process bean : obj_AL) {
                i = 1;
                p.setString(i++, bean.getProcess_id());
                p.setString(i++, bean.getDoc_id());
                p.setString(i++, bean.getLine_no());
                p.setString(i++, bean.getDoc_date());
                p.setString(i++, bean.getDoc_type());
                p.setString(i++, bean.getPart_id());
                p.setString(i++, bean.getPrice_unit());
                p.setString(i++, bean.getQty());
                p.setString(i++, bean.getUnit_id());
                p.setString(i++, bean.getWh_id());
                p.setString(i++, bean.getLocation_id());
                p.setTimestamp(i++, new Timestamp(new java.util.Date().getTime()));
                p.setString(i++, ("System"));
                //System.out.println(i++);
                //System.out.println(bean.getPart_id());
                p.addBatch();
                //p.executeUpdate();
            }
            p.executeBatch();
        } catch (SQLException e) {
        } finally {
            if (p != null) {
                p.clearBatch();
                p.clearParameters();
            }
            obj_AL.clear();
        }
    }

    private void InsTimeStamp(String SQL_TimeStamp, Connection con, PreparedStatement p) throws Exception {
        try {
            p = con.prepareStatement(SQL_TimeStamp);
            p.executeUpdate();
            p.clearParameters();
        } catch (SQLException e) {
        } finally {
        }
    }

    private void delete(String SQL_DEL, Connection con, PreparedStatement p) throws Exception {
        try {
            System.out.println("Delete : " + SQL_DEL);
            p = con.prepareStatement(SQL_DEL);
            p.executeUpdate();
            p.clearParameters();
        } catch (SQLException e) {
        } finally {
        }
    }

    private void Update_LastPartMovement(Connection con, String part_id, String doc_date) throws Exception {

        try (Connection con2 = new DBConnect().openConnection_CMMS_Y()) {
            ResultSet rs2;
            PreparedStatement p;
            String check = "", set_last_movement = "", SQL_Last_Movement = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String SQL_MPart = " select runno,part_id,last_movement from m_part "
                    + " where delete_flag <> 'Y' "
                    + " and part_id = '" + part_id + "'";

            rs2 = con2.createStatement().executeQuery(SQL_MPart);
            while (rs2.next()) {

                if (rs2.getString("last_movement").equals("") || rs2.getString("last_movement") == null || rs2.getString("last_movement").equals("-")) {
                    check = "System-Last 1";
                    set_last_movement = doc_date;
                } else {

                    Date date1 = sdf.parse(DateUtil.ThaiDate_To_EngDate(doc_date));
                    Date date2 = sdf.parse(DateUtil.ThaiDate_To_EngDate(rs2.getString("last_movement")));

                    if (date1.compareTo(date2) >= 0) {
                        //System.out.println("Date1 is after or equals Date2");
                        set_last_movement = doc_date;
                        check = "System-Last 2";
                    } else if (date1.compareTo(date2) < 0) {
                        //System.out.println("Date1 is before Date2");
                        set_last_movement = rs2.getString("last_movement");
                        check = "System-Last 3";
                    }
                }
                //System.out.println("Current Date = " + DateUtil.Return_Date_Now_full());
                SQL_Last_Movement = " update m_part set last_movement = '" + set_last_movement + "'"
                        //+ " ,update_by = '" + check + "'"
                        //+ " ,update_date = '" + new Timestamp(new java.util.Date().getTime()) + "'"
                        + " where runno = " + rs2.getInt("runno");

                System.out.println("SQL_Last_Movement = " + SQL_Last_Movement);

                p = con.prepareStatement(SQL_Last_Movement);
                p.executeUpdate();
                p.clearParameters();

                System.out.println("Final Update_LastPartMovement");
            }
        }
    }

    private void Update_LastPriceUnit(Connection con, String part_id, String doc_date, String price_unit) throws Exception {

        try (Connection con2 = new DBConnect().openConnection_CMMS_Y()) {
            ResultSet rs2;
            PreparedStatement p;
            String check = "", last_update_price = "", SQL_Last_Price;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String SQL_MPart = " select runno,part_id,last_update_price from m_part "
                    + " where delete_flag <> 'Y' "
                    + " and part_id = '" + part_id + "'";

            System.out.println("price_unit = " + price_unit);

            rs2 = con2.createStatement().executeQuery(SQL_MPart);
            while (rs2.next()) {

                if (rs2.getString("last_update_price").equals("") || rs2.getString("last_update_price") == null || rs2.getString("last_update_price").equals("-")) {
                    check = "System-Last 1";
                    last_update_price = doc_date;
                } else {

                    Date date1 = sdf.parse(DateUtil.ThaiDate_To_EngDate(doc_date));
                    Date date2 = sdf.parse(DateUtil.ThaiDate_To_EngDate(rs2.getString("last_update_price")));

                    if (date1.compareTo(date2) >= 0) {
                        //System.out.println("Date1 is after or equals Date2");
                        last_update_price = doc_date;
                        check = "System-Last 2";
                    } else if (date1.compareTo(date2) < 0) {
                        //System.out.println("Date1 is before Date2");
                        last_update_price = rs2.getString("last_update_price");
                        check = "System-Last 3";
                    }
                }
                //System.out.println("Current Date = " + DateUtil.Return_Date_Now_full());

                if (!price_unit.equals("")) {

                    double D_price_unit = Double.parseDouble(price_unit);

                    if (D_price_unit > 0) {
                        SQL_Last_Price = " update m_part set last_update_price = '" + last_update_price + "'"
                                + " ,price = '" + price_unit + "'"
                                + " where runno = " + rs2.getInt("runno");

                        System.out.println("last_update_price = " + SQL_Last_Price);

                        p = con.prepareStatement(SQL_Last_Price);
                        p.executeUpdate();
                        p.clearParameters();
                    }

                }

                System.out.println("Final Update_last_price");
            }
        }
    }


}
