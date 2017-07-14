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

/**
 *
 * @author ball
 */
public class Process_transaction_Old {

    /**
     * @param date_from
     * @param date_to
     * @return
     * @throws java.lang.Exception
     */
    public String main_check(String date_from, String date_to) throws Exception {
        StringBuilder String_return = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss");
        Timestamp cur_time = new Timestamp(new java.util.Date().getTime());
        String start_time = sdf.format(cur_time);

        // TODO code application logic here
        try {
            Random r = new Random();
            Transfer_M_Part_Price obj_transfer_part_price = new Transfer_M_Part_Price();
            Process_transactionDB obj = new Process_transactionDB();
            Process_Stock_Balance_DB obj_STB = new Process_Stock_Balance_DB();
            Process_Price_History obj_PPH = new Process_Price_History();
            String r_create = Long.toString(Math.abs(r.nextLong()), 36);

            obj_transfer_part_price.generater_transaction_trasnfer_process(date_from, date_to, "Part_Price", "m_part_price_month", "X", r_create, "System");

            //เรียกใช้งานให้ส่ง Parameter ตามนี้ obj.generater_transaction_process(job_id);
            obj.generater_transaction_process(date_from, date_to);
            obj.generater_transaction_process2(date_from, date_to);
            obj.generater_transaction_process_oil_withdraw(date_from, date_to);
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
}
