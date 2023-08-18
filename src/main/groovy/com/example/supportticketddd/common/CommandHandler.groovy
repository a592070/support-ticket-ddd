package com.example.supportticketddd.common

interface CommandHandler<T extends Command, E extends CommandResult> {
    E execute(T command)
}