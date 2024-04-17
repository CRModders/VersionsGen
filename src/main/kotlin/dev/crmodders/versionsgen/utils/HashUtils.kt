package dev.crmodders.versionsgen.utils

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class HashUtils private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        @Throws(IOException::class)
        fun sha256(file: Path): String {
            val hashFunction = "SHA-256"

            try {
                Files.newInputStream(file).use { inputStream ->
                    MessageDigest.getInstance(hashFunction).apply {
                        this.update(inputStream.readAllBytes())

                        return HexFormat.of().formatHex(this.digest())
                    }
                }
            } catch (e: NoSuchAlgorithmException) {
                throw IOException("$hashFunction algorithm is not available in your JRE", e)
            }
        }
    }
}
