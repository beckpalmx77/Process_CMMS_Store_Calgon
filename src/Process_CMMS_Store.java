/**
 * Created by beckp on 3/10/2558.
 */
import com.cgc.Util.DateUtil;
import com.cgc.Util.OS_Type;
import com.cgc.Util.PeriodDate;
import com.cgc.Util.ReadConfig;
import com.cgc.engine.Process_transaction;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.io.IOException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

public class Process_CMMS_Store {

    public static void main(String args[]) {

        Process_Transaction();

    }

    private static void Process_Transaction() {
        try {

            Process_transaction objcom = new Process_transaction();

            String username = "System", process_for;


            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            OS_Type os_type = new OS_Type();

            DateUtil ObjDate = new DateUtil();
            PeriodDate period = new PeriodDate();
            String current_date = ObjDate.Return_Date_Now_full();
            String current_month = ObjDate.Return_Month_Now();
            String current_year = ObjDate.Return_Year_Now();

            //System.out.println("Test 6 " + current_month);

            //String date_from = period.Start_Current_Month(current_month);

            String date_from = period.Start_Year("S");
            String date_to = ObjDate.Return_Date_Now_full();

            //String date_to = period.End_Current_Month(current_month);

            System.out.println("os_type = " + os_type.GetOS_Type("Y"));

            if (os_type.GetOS_Type("Y").equals("WIN")) {
                System.out.println("Y date_to = " + date_to);
            } else {
                date_to = ObjDate.EngDate_To_ThaiDate(ObjDate.Return_Date_Now_full());
                System.out.println("N date_to = " + date_to);
            }

            System.out.println("current_month : " + current_month);
            System.out.println("current_year : " + current_year);

            System.out.println("Start Process Date : " + new Timestamp(new java.util.Date().getTime()));

            process_for = "STORE";

            System.out.println("Process date_from : " + date_from);
            System.out.println("Process date_to : " + date_to);

            objcom.main_check(date_from, date_to);

            System.out.println("End Process Date : " + new Timestamp(new java.util.Date().getTime()));


        } catch (Exception ex) {
            System.out.println("ERROR");
        }

    }
}
