package dev.crmodders.versionsgen.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.long
import dev.crmodders.versionsgen.utils.HashUtils
import dev.crmodders.versionsgen.utils.TimeUtils
import java.nio.file.Files
import kotlin.io.path.Path

class FileCommand : CliktCommand(
    name = "file",
    help = "Generate a JSON object for a single file",
) {
    companion object {
        const val DEFAULT_URL = "https://raw.githubusercontent.com/CRModders/CosmicArchive/main/"
    }

    private val path by option("--path", help = "Path to the Cosmic Reach jar")
    private val type by option("--type", help = "Version type").default("pre_alpha")
    private val releaseTime by option("--releaseTime", help = "Unix timestamp").long().default(TimeUtils.getTodayUnixTime())
    private val url by option("--url", help = "Download url").default(DEFAULT_URL)

    override fun run() {
        val path = Path(path!!)
        val fileName = path.fileName.toString()

        println(
            """
            {
                "id": "${parseId(fileName)}",
                "type": "$type",
                "releaseTime": $releaseTime,
                "url": "${url + fileName.replace(" ", "%20")}",
                "hash": "${HashUtils.murmur3(path)}",
                "size": ${Files.size(path)}
            }
        """.trimIndent()
        )
    }

    private fun parseId(fileName: String): String {
        return fileName.split("-")[1].split(".jar")[0]
    }
}