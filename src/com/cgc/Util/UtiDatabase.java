package com.cgc.Util;

import java.sql.Connection;
import java.sql.ResultSet;

import com.cgc.DB.DBConnect;

public class UtiDatabase {

    public int numRowdatabase(String inputSQL) throws Exception//นับข้อมูลใน DataBase
    {

        int a = 0;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            while (rs.next()) {
                a = rs.getInt("num");
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return a;
    }

    public String GetField(String inputSQL) throws Exception//หาชื่อ Field ที่ต้องการ
    {
        String f = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            while (rs.next()) {
                f = rs.getString("Field");
                System.out.println("f = " + f);
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return f;
    }

    public double numSumdatabase(String inputSQL) throws Exception//นับข้อมูลใน DataBase
    {

        double a = 0;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            while (rs.next()) {
                a = rs.getDouble("sum_num");
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return a;
    }

///********************************************โชว์ Combobox แบบ ดึงมาจากฐานข้อมูล
    public String ShowSelectBox(String inputSQL, String Value, String ShowData, String HTMLIDandName) throws Exception {
        String output;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            //output = "<select class=\"form-control\" name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";            
            output = "<select class='select_cgc' name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";
            while (rs.next()) {
                output += "<option value='" + rs.getString(Value) + "'>" + rs.getString(ShowData).trim() + "</option>";
                //System.out.println("Value " + rs.getString(Value));
                //System.out.println("ShowData " + rs.getString(ShowData));
            }
            output += "</select>";
            //System.out.println(output);
        } finally {
            try {
                rs.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return output;
    }

    public String ShowSelectBox(String inputSQL, String Value, String ShowData, String HTMLIDandName, String othervalue, String othershow) throws Exception {
        String output;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            //output = "<select class=\"form-control\" name='" + HTMLIDandName + "'id='" + HTMLIDandName + "'>";            
            output = "<select class='select_cgc' name='" + HTMLIDandName + "'id='" + HTMLIDandName + "'>";
            output += "<option value='" + othervalue + "'>" + othershow.trim() + "</option>";
            while (rs.next()) {
                output += "<option value='" + rs.getString(Value) + "'>" + rs.getString(ShowData).trim() + "</option>";
            }
            output += "</select>";

            //System.out.println("output = " + output);
        } finally {
            try {
                rs.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return output;
    }

    public String ShowSelectBox_Class(String inputSQL, String Value, String ShowData, String HTMLIDandName) throws Exception {
        String output;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            output = "<select class=\"form-control\" data-style=\"btn-info\" name='" + HTMLIDandName + "'id='" + HTMLIDandName + "'>";
            //output = "<select class=\"selectpicker\" data-width=\"30px\" name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";            
            //output = "<select name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";      
            while (rs.next()) {
                output += "<option value='" + rs.getString(Value) + "'>" + rs.getString(ShowData).trim() + "</option>";
                //System.out.println("Value " + rs.getString(Value));
                //System.out.println("ShowData " + rs.getString(ShowData));
            }
            output += "</select>";
            //System.out.println(output);
        } finally {
            try {
                rs.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return output;
    }

    public String ShowSelectBox_Class1(String inputSQL, String Value, String ShowData, String HTMLIDandName, String othervalue, String othershow) throws Exception {
        String output;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            //output = "<select class=\"form-control\" name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";
            output = "<select select class=\"selectpicker\" data-style=\"btn-info\" name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";

            //output = "<select name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";
            while (rs.next()) {
                output += "<option value='" + rs.getString(Value) + "'>" + rs.getString(ShowData).trim() + "</option>";
            }
            output += "</select>";
            //System.out.println(output);
        } finally {
            try {
                rs.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return output;
    }

    public String ShowSelectBox_Class2(String inputSQL, String Value, String ShowData, String HTMLIDandName, String othervalue, String othershow) throws Exception {
        String output;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            //output = "<select class=\"form-control\" name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";
            output = "<select class=\"selectpicker\" name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";

            //output = "<select name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";
            while (rs.next()) {
                output += "<option value='" + rs.getString(Value) + "'>" + rs.getString(ShowData).trim() + "</option>";
            }
            output += "</select>";
            //System.out.println(output);
        } finally {
            try {
                rs.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return output;
    }

    public String ShowSelectBox_Class3(String inputSQL, String Value, String ShowData, String HTMLIDandName) throws Exception {
        String output;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            output = "<select class=\"selectpicker\" name='" + HTMLIDandName + "'id='" + HTMLIDandName + "'>";
            //output = "<select class=\"selectpicker\" data-width=\"30px\" name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";            
            //output = "<select name='" + HTMLIDandName + "'id='" + HTMLIDandName.trim() + "'>";      
            while (rs.next()) {
                output += "<option value='" + rs.getString(Value) + "'>" + rs.getString(ShowData).trim() + "</option>";
                //System.out.println("Value " + rs.getString(Value));
                //System.out.println("ShowData " + rs.getString(ShowData));
            }
            output += "</select>";
            //System.out.println(output);
        } finally {
            try {
                rs.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return output;
    }

    public String ShowSelectBox_Bootstrap(String inputSQL, String Value, String ShowData, String HTMLIDandName, String data, String tooltip_text) throws Exception {
        String output;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            output = "<select class=\"selectpicker multiple\" name='" + HTMLIDandName + "' id='" + HTMLIDandName.trim() + "'" + " data-toggle=\"tooltip\" title= '" + tooltip_text + "'>";
            while (rs.next()) {
                output += "<option value='" + rs.getString(Value) + "'>" + rs.getString(ShowData).trim() + "</option>";
            }
            output += "</select>";
        } finally {
            try {
                rs.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return output;
    }

    //ใช้ตอนจะนำข้อมูลลงฐานข้อมูลข้อมูลจะเป็นแบบ new Line
    public String EncodeNewLine(String input_string) {
        return input_string.replaceAll("<br>", "\n");
    }
    //ใช้ตอนที่ดึงข้อมูลออกจากฐานข้อมูลแล้วทำใส่BR เพื่อใช้ใน Textarea

    public String DecodeNewLine(String input_string) {
        input_string = input_string.replaceAll("\"", "*");
        input_string = input_string.replaceAll("\'", "*");
        return input_string.replaceAll("\n", "<br>");
    }
    //ไม่ให้แสดงคำว่าNull

    public String NotNull(String input_str) {
        if (input_str == null) {
            input_str = "";
        }
        return input_str;
    }

    public String NotNull(String input_str, String ouput_str) {
        return (input_str == null || input_str.equalsIgnoreCase("")) ? ouput_str : input_str;
    }
    ///********************************************โชว์ Combobox แบบ ดึงมาจากฐานข้อมูล(ใน Search_Config2)

    public String ShowSelectBoxSearch(String inputSQL, String Value, String ShowData, String HTMLIDandName) throws Exception {
        String output;
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = con.createStatement().executeQuery(inputSQL);
        try {
            output = "<select class='select_cgc' name='" + HTMLIDandName + "'id='" + HTMLIDandName + "' onclick=SelectBoxSearch(\'" + Value + "\',\'" + HTMLIDandName + "\','stadment')>";
            while (rs.next()) {
                output += "<option value='" + rs.getString(Value) + "'>" + rs.getString(ShowData) + "</option>";
            }
            output += "</select>";
        } finally {
            try {
                rs.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return output;
    }

    public String RunLine_no(String Table_name, String Doc_id) throws Exception {
        String Str_Line_no = "", SQL = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        SQL = "Select line_no from " + Table_name + " where doc_id = '" + Doc_id + "' and delete_flag = 'N'  order by to_number(line_no,'999') desc";
        ResultSet rs = null;
        try {
            rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                if (rs.isFirst()) {
                    Str_Line_no = Integer.toString(rs.getInt("line_no") + 1);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
            return Str_Line_no;
        }
    }

    public String Format_Date_System(String inputDate) {
        return Integer.toString(Integer.parseInt(inputDate.substring(6, 10)) - 543) + "-" + inputDate.substring(3, 5) + "-" + inputDate.substring(0, 2);

    }

    public String End_Date(String Month, String Year) {
        String retrun_Date = "";
        if (Month.equals("04") || Month.equals("06") || Month.equals("09") || Month.equals("11")) {
            retrun_Date = Year + "-" + Month + "-" + "30";
        } else if (Month.equals("02")) {
            if ((Integer.parseInt(Year) % 4) == 0) {
                retrun_Date = Year + "-" + Month + "-" + "29";
            } else {
                retrun_Date = Year + "-" + Month + "-" + "28";
            }
        } else {
            retrun_Date = Year + "-" + Month + "-" + "31";
        }
        return retrun_Date;
    }

    public String End_Date(String Month, String Year, String Location) {
        String retrun_Date = "";
        if (Location.equals("TH")) {
            if (Month.equals("04") || Month.equals("06") || Month.equals("09") || Month.equals("11")) {
                retrun_Date = "30-" + Month + "-" + (Integer.parseInt(Year) + 543);
            } else if (Month.equals("02")) {
                if ((Integer.parseInt(Year) % 4) == 0) {
                    retrun_Date = "29-" + Month + "-" + (Integer.parseInt(Year) + 543);
                } else {
                    retrun_Date = "28-" + Month + "-" + (Integer.parseInt(Year) + 543);
                }
            } else {
                retrun_Date = "31-" + Month + "-" + (Integer.parseInt(Year) + 543);
            }
            return retrun_Date;
        } else {
            if (Month.equals("04") || Month.equals("06") || Month.equals("09") || Month.equals("11")) {
                retrun_Date = Year + "-" + Month + "-" + "30";
            } else if (Month.equals("02")) {
                if ((Integer.parseInt(Year) % 4) == 0) {
                    retrun_Date = Year + "-" + Month + "-" + "29";
                } else {
                    retrun_Date = Year + "-" + Month + "-" + "28";
                }
            } else {
                retrun_Date = Year + "-" + Month + "-" + "31";
            }
        }
        return retrun_Date;
    }

    public String Count_sendcomplete(String Table_name, String Doc_id, String Complete_flag, Connection con) throws Exception {
        ResultSet rs = null;
        String SQL = "", Return = "0";
        UtiDatabase objuti = new UtiDatabase();
        try {
            if (Complete_flag.equalsIgnoreCase("Y")) {
                SQL = "select count(doc_id) as num from " + Table_name + " where doc_id = '" + Doc_id + "' and delete_flag = 'N' and complete_flag = 'N'";
                if (objuti.numRowdatabase(SQL) != 0) {
                    SQL = "select count_send_complete from " + Table_name + " where doc_id = '" + Doc_id + "' and delete_flag = 'N' and complete_flag = 'N'";
                    rs = con.createStatement().executeQuery(SQL);
                    while (rs.next()) {
                        Return = Integer.toString(Integer.parseInt(rs.getString("count_send_complete")) + 1);
                    }

                } else {
                    Return = "1";
                }
            } else {
                SQL = "select count(doc_id) as num from " + Table_name + " where doc_id = '" + Doc_id + "' and delete_flag = 'N' and complete_flag = 'N'";
                if (objuti.numRowdatabase(SQL) != 0) {
                    SQL = "select count_send_complete from " + Table_name + " where doc_id = '" + Doc_id + "' and delete_flag = 'N' and complete_flag = 'N'";
                    rs = con.createStatement().executeQuery(SQL);
                    while (rs.next()) {
                        Return = rs.getString("count_send_complete");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);

        } finally {
            if (rs != null) {
                rs.close();
            }

            return Return;
        }
    }

    //2556-01-01 => 01-01-2013
    public String Format_Date_For_Mysql(String inputdate) {
        return inputdate.substring(8, 10) + "-" + inputdate.substring(5, 7) + "-" + (Integer.parseInt(inputdate.substring(0, 4)) - 543);
    }

    public String ShowSelectBoxSearch(String Select_value, String[] Fill_Name, String[] Value, String HTMLIDandName) throws Exception {
        String string_output = new String();
        try {
            string_output = "<select class='select_cgc' name='" + HTMLIDandName + "'id='" + HTMLIDandName + "' onclick=\"Click_Select_Search(this)\">";
            for (int i = 0; i < Fill_Name.length; i++) {
                string_output += "<option " + (Value[i].equals(Select_value) ? "selected" : "") + " value='" + Value[i] + "' >" + Fill_Name[i] + "</option>";
            }
            string_output += "</select>";
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            return string_output;
        }
    }

    public String Last_Create_Date(String Table_name) throws Exception {
        String Str_Return = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        String inputSQL = " select create_date From " + Table_name + " order by runno desc limit 1";
        ResultSet rs = null;
        try {
            rs = con.createStatement().executeQuery(inputSQL);
            while (rs.next()) {
                Str_Return = rs.getString("create_date");
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
            return Str_Return;
        }
    }
    
    public String Last_Create_Date2(String inputSQL) throws Exception {
        String Str_Return = "";
        DBConnect objcon = new DBConnect();
        Connection con = objcon.openConnection_CMMS_Y();
        ResultSet rs = null;
        try {
            rs = con.createStatement().executeQuery(inputSQL);
            while (rs.next()) {
                Str_Return = rs.getString("create_date");
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
            return Str_Return;
        }
    }    

}
