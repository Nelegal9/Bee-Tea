package com.alekhin.beetea.domain

import java.io.IOException

class TransferFailedException: IOException("Reading incoming data failed")