package com.timmy.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		//Long time=(long) 20161030000000;
          Date date =new Date("Sat Nov 11 00:00:00 CST 2017");
          System.out.println(date);
          SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
          String dString=dateFormat.format(date);
          String []dStrings=dString.split("-");
          System.out.println(dStrings[4].equals("00"));
          
	}

}
