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
package com.seomse.jdbc.example.objects;

import com.seomse.jdbc.objects.JdbcObjects;

/**
 * @author macle
 */
public class UpdateExample {
    public static void main(String [] args){
        ExampleItem exampleItem= new ExampleItem();
        exampleItem.setCode("test5");
        exampleItem.setName("테스트5uuu");
//        JdbcObjects.insertOrUpdate(exampleItem, false);
        JdbcObjects.update(exampleItem, false);
    }
}
