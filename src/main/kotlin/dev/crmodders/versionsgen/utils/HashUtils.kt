package dev.crmodders.versionsgen.utils

import org.apache.commons.codec.digest.MurmurHash3
import java.io.IOException
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class HashUtils private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        @Throws(IOException::class)
        fun murmur3(file: Path): String {
            val longs = MurmurHash3.hash128x64(Files.readAllBytes(file))

            val bytes = ByteArray(java.lang.Long.BYTES * longs.size)
            val byteBuffer = ByteBuffer.wrap(bytes)

            longs.forEach { byteBuffer.putLong(it) }

            return BigInteger(bytes).abs().toString(16)
        }
    }
}
