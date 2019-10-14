package ru.nbdev.kotlinnotes

import org.mockito.Mockito

fun <T> notNullAny(type : Class<T>): T {
    Mockito.any(type)
    return null as T
}