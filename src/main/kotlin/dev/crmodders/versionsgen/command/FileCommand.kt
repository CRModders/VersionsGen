package dev.crmodders.versionsgen.command

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.CliktCommand
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

class FileCommand : CliktCommand(
    help = "Copy archives in place and print JSON entry",
    epilog = """
        The archive folder is `./versions/[type]/[number]/[client|server]/`, so
        an example would be `./versions/pre-alpha/0.3.1/client/`. The JSON entry
        generated will only contain the specified JAR side and it won't infer
        from already existing files. Specifying client will only show the client
        data, server-only for server, and both when client and server locations
        are specified. Though technically fine, it is not possible to print the
        entry JSON without specifying any archives.
    """.trimIndent(),
    name = "file",
    printHelpOnEmptyArgs = true,
) {
    companion object {
        const val DEFAULT_BASE_URL = "https://raw.githubusercontent.com/CRModders/CosmicArchive/main/"
    }

    private val versionType by option(
        "-t", "--type", "--version-type",
        help = "Required: version type",
        completionCandidates = CompletionCandidates.Fixed("pre-alpha"),
    ).required()

    private val versionNumber by option(
        "-v", "--version", "--version-number",
        help = "Required: version number",
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
                "type": "$versionType",
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
        val url = "${baseUrl}versions/${formatVersionType(versionType)}/$versionNumber/$side/$fileName"

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

    private fun formatVersionType(type: String): String {
        val formattedType = type.replace("_", "-")
        return if (formattedType.contains("_")) formatVersionType(formattedType) else {
            return formattedType
        }
    }
}