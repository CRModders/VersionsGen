package dev.crmodders.versionsgen.command.file

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.long
import dev.crmodders.versionsgen.utils.HashUtils
import dev.crmodders.versionsgen.utils.TimeUtils
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.system.exitProcess

class V2FileCommand : CliktCommand("v2") {
    override fun help(context: Context) = "Version 2 is stored in `versions_v2.json`"
    override val printHelpOnEmptyArgs = true

    companion object {
        const val DEFAULT_BASE_URL = "https://raw.githubusercontent.com/CRModders/CosmicArchive/main/"
    }

    private val versionNumber by option(
        "-v", "--version", "--version-number",
        help = "Required: version number",
    ).required()

    private val versionType by option(
        "-t", "--type", "--version-type",
        help = "Required: version type",
        completionCandidates = CompletionCandidates.Fixed("release", "snapshot"),
    ).required()

    private val versionPhase by option(
        "-p", "--phase", "--version-phase",
        help = "Required: version phase",
        completionCandidates = CompletionCandidates.Fixed("pre-alpha"),
    ).required()

    private val client by option(
        "-c", "--client",
        help = "Client JAR path",
        completionCandidates = CompletionCandidates.Path,
    ).convert { Path.of(it) }

    private val server by option(
        "-s", "--server",
        help = "Server JAR path",
        completionCandidates = CompletionCandidates.Path,
    ).convert { Path.of(it) }

    private val baseUrl by option(
        "--url", "--base-url",
        help = "Download url",
    ).default(DEFAULT_BASE_URL)

    private val releaseTime by option(
        "--release-time",
        help = "Unix timestamp",
    ).long().default(TimeUtils.getTodayUnixTime())

    override fun run() {
        if (client == null && server == null) {
            System.err.println("Neither client nor server JAR was specified")
            System.err.println("It is required to specify either --client, --server, or both")
            exitProcess(1)
        }

        println(buildEntryJson())
    }

    private fun buildEntryJson(): Any {
        val builder = StringBuilder()

        builder.append(
            """
            {
                "id": "$versionNumber",
                "type": "${versionType.replace("-", "_")}",
                "phase": "${versionPhase.replace("-", "_")}",
                "releaseTime": $releaseTime
        """.trimIndent()
        )

        client?.let { buildEntrySide(builder, it, "client") }
        server?.let { buildEntrySide(builder, it, "server") }

        return builder.append(
            """
            
            }
        """.trimIndent()
        )
    }

    private fun buildEntrySide(builder: StringBuilder, path: Path, side: String) {
        val fileName = path.fileName.toString().replace(" ", "%20")
        val url = "${baseUrl}versions/${versionPhase.replace("_", "-")}/$versionNumber/$side/$fileName"

        builder.append(
            """
            ,
                "$side": {
                    "url": "$url",
                    "sha256": "${HashUtils.sha256(path)}",
                    "size": ${path.fileSize()}
                }
        """.trimIndent())
    }
}