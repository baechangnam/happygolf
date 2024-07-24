package com.artilearn.happygolfgo.ui.golfgame.model

data class GolfGameModel(
    val club: String,
    val averageScore: String,
    val highScore: String,
    val scores: List<Float?>,
    val dates: List<String?>
)