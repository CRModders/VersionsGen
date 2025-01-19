package dev.crmodders.versionsgen.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.subcommands
import dev.crmodders.versionsgen.command.file.V1FileCommand
import dev.crmodders.versionsgen.command.file.V2FileCommand

class FileCommand : CliktCommand("file") {
    override fun help(context: Context) = "Copy archives in place and print JSON entry"
    override fun helpEpilog(context: Context) = """
        The archive folder is `./versions/[type]/[number]/[client|server]/`, so
        an example would be `./versions/pre-alpha/0.3.1/client/`. The JSON entry
        generated will only contain the specified JAR side and it won't infer
        from already existing files. Specifying client will only show the client
        data, server-only for server, and both when client and server locations
        are specified. Though technically fine, it is not possible to print the
        entry JSON without specifying any archives.
    """.trimIndent()
    override val printHelpOnEmptyArgs = true

    override fun run() = Unit

    init {
        subcommands(V1FileCommand())
        subcommands(V2FileCommand())
    }
}