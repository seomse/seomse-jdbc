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
package com.seomse.jdbc.example.naming;

import com.seomse.jdbc.naming.JdbcNaming;

import java.util.List;

/**
 * @author macle
 */
public class SelectExample {

    public static void main(String[] args) {

        List<ItemNo> itemNoList = JdbcNaming.getObjList(ItemNo.class);


        for(ItemNo itemNo : itemNoList){
            System.out.println(itemNo.getITEM_CD() + ", " + itemNo.getITEM_NM());
        }


    }

}
