package com.alekhin.beetea.domain

data class BluetoothMessage(val fromLocalUser: Boolean, val senderName: String, val message: String)

fun BluetoothMessage.toByteArray(): ByteArray {
    return "$senderName#$message".encodeToByteArray()
}

fun String.toBluetoothMessage(fromLocalUser: Boolean): BluetoothMessage {
    val senderName = substringBefore("#")
    val message = substringAfterLast("#")
    return BluetoothMessage(fromLocalUser, senderName, message)
}