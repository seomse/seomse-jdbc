package com.seomse.jdbc.sequence;

public class EmptySquenceMaker implements SequenceMaker {
    @Override
    public String nextVal(String sequenceName) {
        return null;
    }
}
