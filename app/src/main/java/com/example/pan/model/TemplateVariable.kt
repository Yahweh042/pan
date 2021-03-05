package com.example.pan.model

data class TemplateVariable(
    val errno: Int,
    val request_id: Long,
    val result: Result
)

data class Result(
    val sign1: String,
    val sign2: String,
    val sign3: String,
    val timestamp: Long
)