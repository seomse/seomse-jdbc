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
package com.seomse.jdbc.common;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * jdbc close util
 * @author macle
 */
public class JdbcClose {

    /**
     * Statement, ResultSet close
     * @param statement Statement
     * @param resultSet ResultSet
     */
    public static void statementResultSet(Statement statement, ResultSet resultSet ){

        if(statement != null){
            //noinspection CatchMayIgnoreException
            try{statement.close();}catch(Exception e){}
        }

        if(resultSet != null){
            //noinspection CatchMayIgnoreException
            try{resultSet.close();}catch(Exception e){}
        }

    }

}
