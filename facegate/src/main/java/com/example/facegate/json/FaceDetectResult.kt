package com.example.facegate.json


data class FaceDetectFail(
    val error_message: String,
    val request_id: String,
    val time_used: Int
)

data class FaceDetectSuccess(
    val face_num: Int,
    val faces: List<Face>,
    val image_id: String,
    val request_id: String,
    val time_used: Int
)

data class Face(
    val attributes: Attributes,
    val face_rectangle: FaceRectangle,
    val face_token: String
)

data class Attributes(
    val age: Age,
    val gender: Gender,
    val glass: Glass,
    val headpose: Headpose,
    val smile: Smile
)

data class FaceRectangle(
    val height: Int,
    val left: Int,
    val top: Int,
    val width: Int
)

data class Age(
    val value: Int
)

data class Gender(
    val value: String
)

data class Glass(
    val value: String
)

data class Headpose(
    val pitch_angle: Double,
    val roll_angle: Double,
    val yaw_angle: Double
)

data class Smile(
    val threshold: Double,
    val value: Double
)