package com.tiamaes.cloud.netty.command.handler;

import org.springframework.stereotype.Component;

import com.tiamaes.cloud.netty.command.LockedCommand;

@Component("com.tiamaes.cloud.netty.command.LockedCommand")
public class LockedCommandHandler implements CommandHandler<LockedCommand> {

	@Override
	public void execute(LockedCommand command) throws Exception {
		System.out.println("LockedCommand has been executed.");
	}

}
