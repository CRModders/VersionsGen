package dev.crmodders.versionsgen.utils

import java.net.URI
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.*

class ArchiveCopyHandler(private val versionType: String, private val versionNumber: String) {
    private val exceptions = mutableListOf<Pair<String, Exception>>()

    private fun create(s: String, side: String): Path {
        // Prefer directly operating on files if possible
        val path = try {
            URI(s).toPath()
        } catch (_: Exception) {
            try {
                Path(s)
            } catch (_: Exception) {
                null
            }
        }?.toAbsolutePath()?.normalize()

        // Operate directly on files since it's smarter at skipping self-copying
        // Don't fall back to URLs when path is absolute
        return if (path != null && (path.exists() || path.isAbsolute.also {
            if (!path.exists()) {
                throw java.nio.file.NoSuchFileException(s, path.toString(), null)
            }
        })) {
            if (!path.isRegularFile()) {
                throw java.nio.file.NoSuchFileException(s, path.toString(), "not a file")
            }

            val dest = Path("versions", versionType, versionNumber, side)
                .createDirectories()
                .resolve(path.fileName)
            path.copyTo(dest, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING)
            dest
        } else {
            val uri = URI(s)
            val url = if (uri.isAbsolute) {
                uri
            } else {
                URI("https", s, null)
            }.normalize().toURL()

            val fileName = url.path.substringAfterLast('/')
            val dest = Path("versions", versionType, versionNumber, side, fileName)

            dest.createParentDirectories().outputStream().buffered().use { out ->
                url.openStream().buffered().use { it.transferTo(out) }
            }
            dest
        }
    }

    fun tryCreate(s: String, side: String): Path? {
        return try {
            this.create(s, side)
        } catch (cause: Exception) {
            this.exceptions.add(side to cause)
            null
        }
    }

    fun isFailing(): Boolean = exceptions.isNotEmpty()

    fun printExceptions() = exceptions.forEach { (side, cause) ->
        System.err.println("Unable create $side archive: $cause")
    }

    fun takeExceptions(): Exception? {
        val iterator = exceptions.iterator()
        if (!iterator.hasNext()) {
            return null
        }
        val (_, cause) = iterator.next()
        iterator.forEach { (_, other) -> cause.addSuppressed(other) }
        return cause
    }
}