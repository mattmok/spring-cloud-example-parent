package com.tiamaes.cloud.netty.command.handler;

import org.springframework.stereotype.Component;

import com.tiamaes.cloud.netty.command.UnlockCommand;

@Component("com.tiamaes.cloud.netty.command.UnlockCommand")
public class UnlockCommandHandler implements CommandHandler<UnlockCommand> {

	@Override
	public void execute(UnlockCommand command) throws Exception {
		System.out.println("UnlockCommand has been executed.");
	}

}
