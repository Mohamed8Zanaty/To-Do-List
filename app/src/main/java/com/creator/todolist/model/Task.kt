package com.creator.todolist.model

class Task {
    var id: Int = 0
    lateinit var text: String
    var status: Int = 0

    constructor() // Default constructor

    constructor(id: Int, text: String, status: Int) : this() {
        this.id = id
        this.text = text
        this.status = status
    }
}