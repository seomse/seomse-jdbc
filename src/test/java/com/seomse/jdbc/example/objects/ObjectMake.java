package com.seomse.jdbc.example.objects;

import com.seomse.jdbc.objects.JdbcObjects;


public class ObjectMake {

    public static void main(String[] args) {

        String tableName = "TB_STOCK_ITEM";
        System.out.println("@Table(name=\"" +  tableName+ "\")\n");
        System.out.println(JdbcObjects.makeObjectValue(tableName));

    }
}
