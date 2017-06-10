package com.tiamaes.cloud.netty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.tiamaes.cloud.netty.command.Command;
import com.tiamaes.cloud.netty.command.UnlockCommand;
import com.tiamaes.cloud.netty.command.handler.CommandHandler;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandTest {

	@Autowired
	private ApplicationContext context;
	
	
	@Test
	public void test() throws Exception {
		Command command = new UnlockCommand();
		
		@SuppressWarnings("unchecked")
		CommandHandler<Command> handler = context.getBean(command.getClass().getName(), CommandHandler.class);
		
		handler.execute(command);
	}
}
