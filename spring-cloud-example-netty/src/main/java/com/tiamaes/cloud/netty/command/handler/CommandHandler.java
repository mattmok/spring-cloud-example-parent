package com.tiamaes.cloud.netty.command.handler;

import com.tiamaes.cloud.netty.command.Command;

public interface CommandHandler<T extends Command> {

	public void execute(T command) throws Exception;
}