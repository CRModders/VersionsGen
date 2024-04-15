package dev.crmodders.versionsgen

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import dev.crmodders.versionsgen.command.FileCommand

fun main(args: Array<String>) = object : CliktCommand() {
    override fun run() = Unit
}
    .subcommands(
        FileCommand()
    )
    .main(args)