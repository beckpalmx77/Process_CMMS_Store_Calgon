/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgc.Util;

/**
 *
 * @author Beckeckpalmx
 */
public class OS_Type {

    public String GetOS_Type(String Status) throws Exception//Get OS Type
    {
        String OS = System.getProperty("os.name").substring(0, 3).toUpperCase();
        return OS;
    }

    public String GetPath(String OS) throws Exception//Get Path
    {
        String exp_path = (OS.equals("WIN")?"D:\\Temp_Upload\\File_Excel\\" : "/home/share-disk/cgc/file_excel/") ;
        return exp_path;
    }

}
