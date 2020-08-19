
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author macle
 */
public class InsertExample {
	public static void main(String [] args){



		List<ExampleItem> itemList =new ArrayList<>();

		StockItem stockItem= new StockItem();
		stockItem.setCode("test1");
		stockItem.setName("테스트1");
		stockItem.setLastUpdateTime(System.currentTimeMillis());
//		itemList.add(exampleItem);
		JdbcObjects.insert(stockItem);
		
	}
}
