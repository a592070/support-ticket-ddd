package com.example.supportticketddd.supportTicket.entity

class TimeLimit {
    long limit


    long value(){
        return limit
    }

    static TimeLimit valueOf(long limit){
        return new TimeLimit(limit: limit)
    }

    static TimeLimit fromLevel(Level level){
        switch (level){
            case Level.LOW:
                return valueOf(12*24*60*60*1000)
            case Level.MEDIUM:
                return valueOf(9*24*60*60*1000)
            case Level.HIGH:
                return valueOf(6*24*60*60*1000)
            case Level.EMERGENCY:
                return valueOf(3*24*60*60*1000)
            default:
                throw new IllegalArgumentException("Not accept argument: level=${level}")
        }
    }
}
