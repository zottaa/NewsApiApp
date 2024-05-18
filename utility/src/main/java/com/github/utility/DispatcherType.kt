package com.github.utility

import jakarta.inject.Qualifier

enum class DispatcherType {
    IO, Main
}

@Qualifier
annotation class Dispatcher(val type: DispatcherType)
