/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgc.engine;

import com.cgc.DB.Process_transactionDB;
import com.cgc.DB.Process_Stock_Balance_DB;
import com.cgc.DB.Process_Price_History;
import com.cgc.DB.Transfer_M_Part_Price;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

import com.cgc.Util.System_Log;

/**
 * @author ball
 */
public class Process_transaction {

    /**
     * @param date_from
     * @param date_to
     * @return
     * @throws java.lang.Exception
     */

    System_Log Log = new System_Log();
    String Final_MSG = "Final-Process OK PR_001";
    Random r = new Random();
    String r_create = Long.toString(Math.abs(r.nextLong()), 36);

    Process_transactionDB obj = new Process_transactionDB();
    Transfer_M_Part_Price obj_transfer_part_price = new Transfer_M_Part_Price();

    public String main_check(String date_from, String date_to) throws Exception {
        StringBuilder String_return = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss");
        Timestamp cur_time = new Timestamp(new java.util.Date().getTime());
        String start_time = sdf.format(cur_time);

        // TODO code application logic here
        try {

            Process_Stock_Balance_DB obj_STB = new Process_Stock_Balance_DB();
            Process_Price_History obj_PPH = new Process_Price_History();
            String Sql_Ins = "insert into t_process_log_transaction (log_id,process_id,start_time,end_time,create_date,create_by,remark) "
                    + "values ('" + r_create + "','" + "STOCK" + "','" + cur_time + "','" + cur_time + "','" + cur_time + "','" + "System" + "','START')";

            System.out.println("Sql_Ins = " + Sql_Ins);

            Log.WriteTimeStamp(Sql_Ins);

            //เรียกใช้งานให้ส่ง Parameter ตามนี้ obj.generater_transaction_process(job_id);

            startProcess(date_from, date_to, "PR_001", "vd_adjust_stock_detail", "+", r_create, "System");
            startProcess(date_from, date_to, "PR_002", "vd_stock_withdraw_detail_store_trans", "-", r_create, "System");
            startProcess(date_from, date_to, "PR_003", "vd_oil_withdraw", "-", r_create, "System");

            startProcess(date_from, date_to, "PR_TRANSFER", "m_part_price_month", "-", r_create, "System");

            obj_STB.generater_transaction_stock_process(date_from, date_to);
            obj_PPH.generater_price_history(date_from, date_to);

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        Timestamp cur_time2 = new Timestamp(new java.util.Date().getTime());
        String stop_time = sdf.format(cur_time2);
        System.out.println("END Transaction Process ... " + '\n');
        String_return.append("ประมวลผลเสร็จสิ้น " + '\n');
        String_return.append("เริ่มประมวลผลเวลา : " + start_time + '\n');
        String_return.append("เสร็จสิ้นเวลา :           " + stop_time);

        return String_return.toString();

    }


    public void startProcess(final String date_from, final String date_to, final String process_id, final String table_name, final String transaction_type, final String log_id, final String username) throws Exception {

        Runnable runnable;
        runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    try {
                        try {
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            System.out.println("Info : " + e);
                        }

                        System.out.println("Log Data : " + date_from + " - " + date_to + " - " + process_id + " - " + table_name + " - " + transaction_type + " - " + log_id + " - " + username);

                        switch (process_id) {
                            case "PR_001":
                                obj.generater_transaction_process(date_from, date_to);
                                System.out.println("Case Process ID : " + process_id);
                                break;
                            case "PR_002":
                                obj.generater_transaction_process2(date_from, date_to);
                                System.out.println("Case Process ID : " + process_id);
                                break;
                            case "PR_003":
                                obj.generater_transaction_process_oil_withdraw(date_from, date_to);
                                System.out.println("Case Process ID : " + process_id);
                                break;
                            case "PR_TRANSFER":
                                obj_transfer_part_price.generater_transaction_trasnfer_process(date_from, date_to, "Part_Price", "m_part_price_month", "X", log_id, "System");
                        }

                        System.out.println("Out Log Data : " + date_from + " - " + date_to + " - " + process_id + " - " + table_name + " - " + transaction_type + " - " + log_id + " - " + username);
                    } catch (Exception ex) {
                        System.out.println("1 Exeption ... ");
                        //Logger.getLogger(Process_transaction_wh_summary.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // String Str_Proc = "XXX process_id = " + process_id;

                    String Str_Proc = "Final-Process OK " + process_id;

                    System.out.println(Str_Proc);

                    Timestamp cur_time2 = new Timestamp(new java.util.Date().getTime());
                    String Update_Str = "";
                    if (Str_Proc.equals(Final_MSG)) {
                        System.out.println(Str_Proc);
                        Update_Str = " ,remark = 'FINISH' ";
                    }

                    String Sql_Update = "Update t_process_log_transaction set end_time = '" + cur_time2 + "',update_date = '"
                            + cur_time2 + "',update_by = '" + username + "',condition = '" + process_id + "'"
                            + Update_Str + " where log_id = '" + log_id + "'";

                    Log.WriteTimeStamp(Sql_Update);

                } catch (Exception ex) {
                    System.out.println("2 Exeption ... ");
                    //Logger.getLogger(Process_transaction_rawmat_friction_summary.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
        };
        new Thread(runnable).start();
        //System.out.println("Thread Run ...");
    }

    private void Final_Process(String Time_Loop) throws Exception {
        System.out.println("Final_Process ..." + Time_Loop);
    }

}
