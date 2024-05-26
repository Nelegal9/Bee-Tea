package com.alekhin.beetea.data

import android.bluetooth.BluetoothSocket
import com.alekhin.beetea.domain.BluetoothMessage
import com.alekhin.beetea.domain.TransferFailedException
import com.alekhin.beetea.domain.toBluetoothMessage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTransferService(private val socket: BluetoothSocket) {
    fun listenForIncomingMessages(): Flow<BluetoothMessage> {
        return flow {
            if (!socket.isConnected) return@flow
            val buffer = ByteArray(1024)
            while(true) {
                val byteCount = try { socket.inputStream.read(buffer) } catch(e: IOException) { throw TransferFailedException() }
                emit(buffer.decodeToString(endIndex = byteCount).toBluetoothMessage(false))
            }
        }.flowOn(IO)
    }

    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(IO) {
            try { socket.outputStream.write(bytes) } catch(e: IOException) { return@withContext false }
            true
        }
    }
}