package com.troy.tersive.model.repo

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor() {

    val userIndex get() = _userIndex

    private var _userIndex = 0
}
