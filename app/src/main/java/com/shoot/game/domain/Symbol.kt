package com.shoot.game.domain

import com.shoot.game.core.library.XY

data class Symbol (
    override var x: Float,
    override var y: Float,
    val value: Int,
    var time: Int
):XY