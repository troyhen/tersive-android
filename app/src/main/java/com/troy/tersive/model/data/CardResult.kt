package com.troy.tersive.model.data

import java.time.Duration

enum class CardResult(val sortAdd: Int, val timeAdd: Duration) {
    EASY(160, Duration.ofDays(4)),
    GOOD(80, Duration.ofDays(1)),
    HARD(40, Duration.ofHours(2)),
    AGAIN(20, Duration.ofMinutes(15))
}