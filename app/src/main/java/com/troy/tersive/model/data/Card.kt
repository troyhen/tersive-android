package com.troy.tersive.model.data

import com.troy.tersive.model.db.entity.Learn
import com.troy.tersive.model.db.entity.Tersive

data class Card(val front: Boolean, val index: Int, val learn: Learn, val tersive: Tersive)