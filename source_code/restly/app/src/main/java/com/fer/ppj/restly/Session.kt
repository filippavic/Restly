package com.fer.ppj.restly

import java.sql.Timestamp

class Session{
    var id: Int = 0
    var exercise_time: Int = 0
    var total_time: Int = 0
    var date: Timestamp = Timestamp(0)

    constructor(exercise_time: Int, total_time: Int, date: Timestamp){
        this.exercise_time = exercise_time
        this.total_time = total_time
        this.date = date
    }

    constructor(){
    }
}