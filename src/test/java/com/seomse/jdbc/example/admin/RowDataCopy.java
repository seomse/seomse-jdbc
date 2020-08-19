/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seomse.jdbc.example.admin;

import com.seomse.jdbc.admin.RowDataInOut;
import com.seomse.jdbc.connection.ConnectionFactory;

import java.sql.Connection;

/**
 * @author macle
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
