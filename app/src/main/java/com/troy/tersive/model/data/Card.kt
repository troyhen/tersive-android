package com.troy.tersive.model.data

import com.troy.tersive.model.db.tersive.Tersive
import com.troy.tersive.model.db.user.entity.Learn

data class Card(val front: Boolean, val index: Int, val learn: Learn, val tersiveList: List<Tersive>)