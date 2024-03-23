package com.alekhin.beetea.chat

sealed interface ConnectionResult {
    data object ConnectionEstablished: ConnectionResult
    data class Error(val message: String): ConnectionResult
}