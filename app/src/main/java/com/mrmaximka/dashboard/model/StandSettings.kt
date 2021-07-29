package com.mrmaximka.dashboard.model

data class StandSettings(
    var name: String = "",
    var timezone: String = "",
    var description: String = "",
    var delay: Int? = null,
    var wait_time: Int? = null,
    var stop: Int? = null,
    var replay: Int? = null,
    var stopPlayOne: Int? = null,
    var stopPlayTwo: Int? = null,
    var breakTwo: Int? = null,
    var sensitivity: Int? = null
)