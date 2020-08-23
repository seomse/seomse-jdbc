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
package com.seomse.jdbc.sequence;
/**
 * 시퀀스를 돌려 주지 못함
 * database 유형이 잘못 되었을때
 * @author macle
 */
public class EmptySequenceMaker implements SequenceMaker {
    @Override
    public String nextVal(String sequenceName) {
        return null;
    }
}
