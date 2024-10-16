package dev.crmodders.versionsgen.command

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.long
import dev.crmodders.versionsgen.utils.ArchiveCopyHandler
import dev.crmodders.versionsgen.utils.HashUtils
import dev.crmodders.versionsgen.utils.TimeUtils
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.system.exitProcess

class FileCommand : CliktCommand(
    help = "Copy archives in place and print JSON entry",
    epilog = """
        Specified archives are automatically placed in the correct location. The
        archive folder is `./versions/[type]/[number]/[client|server]/`, so an
        example would be `./versions/pre-alpha/0.3.1/client/`. Archive files
        retain their original name. For the tool to properly place the archives,
        try running or even placing the executable at the root of the archive
        structure.
        
        URL archive locations may occasionally fail and most likely due to
        antivirus software. Try running a second time and see if the connection
        would go through.
        
        The JSON entry generated will only contain the specified JAR side and it
        won't infer from already existing files. Specifying client will only
        show the client data, server-only for server, and both when client and
        server locations are specified. Though technically fine, it is not
        possible to print the entry JSON without specifying any archives.
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
        help = "Client JAR path or URL",
        completionCandidates = CompletionCandidates.Path,
    )

    private val server by option(
        "-s", "--server",
        help = "Server JAR path or URL",
        completionCandidates = CompletionCandidates.Path,
    )

    private val baseUrl by option(
        "--url", "--base-url",
        help = "Download url",
    ).default(DEFAULT_BASE_URL)

    private val releaseTime by option(
        "--release-time",
        help = "Unix timestamp",
    ).long().default(TimeUtils.getTodayUnixTime())

    private val stacktrace by option("--stacktrace", help = "Stacktrace on failure").flag()

    override fun run() {
        if (client == null && server == null) {
            System.err.println("Neither client nor server JAR was specified")
            System.err.println("It is required to specify either --client, --server, or both")
            exitProcess(1)
        }

        val handler = ArchiveCopyHandler(versionType, versionNumber)
        val client = client?.let { handler.tryCreate(it, "client") }
        val server = server?.let { handler.tryCreate(it, "server") }
        if (handler.isFailing()) {
            handler.printExceptions()
            System.err.println()
            if (stacktrace) {
                throw handler.takeExceptions()!!
            } else {
                System.err.println("Add --stacktrace to view stacktrace")
            }
            exitProcess(1)
        }

        print(
            """
            {
                "id": "$versionNumber",
                "type": "$versionType",
                "releaseTime": $releaseTime
        """.trimIndent()
        )
        client?.let { printArchiveEntry(it, "client") }
        server?.let { printArchiveEntry(it, "server") }
        println(
            """
            
            }
        """.trimIndent()
        )
    }

    private fun printArchiveEntry(path: Path, side: String) {
        print(
            """
            ,
                "$side": {
                    "url": "${URI(baseUrl + path.joinToString("/")).toURL()}",
                    "sha256": "${HashUtils.sha256(path)}",
                    "size": ${path.fileSize()}
                }
        """.trimIndent())
    }
}