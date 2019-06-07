package com.seomse.jdbc.example;

import com.seomse.jdbc.admin.RowDataInOut;
import com.seomse.jdbc.connection.ConnectionFactory;

import java.sql.Connection;

/**
 * <pre>
 *  파 일 명 : RowDataCopy.java
 *  설    명 : data copy example
 *
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2019.06.07
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class RowDataCopy {
    public static void main(String[] args) {
        try{

            Connection selectConn = ConnectionFactory.newConnection("oracle", "jdbc:oracle:thin:@127.0.0.1:1521:orcl", "select", "select");
            Connection insertConn = ConnectionFactory.newConnection("oracle", "jdbc:oracle:thin:@127.0.0.1:1521:orcl", "insert", "insert");

            String [] tables =
                    {"TABLE_NAME"}
           ;
            RowDataInOut rowDataInOut = new RowDataInOut();

            rowDataInOut.tableCopy(selectConn, insertConn,tables);


        }catch(Exception e){
            e.printStackTrace();;
        }
    }
}
