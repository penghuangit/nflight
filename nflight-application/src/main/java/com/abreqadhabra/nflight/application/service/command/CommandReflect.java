package com.abreqadhabra.nflight.application.service.command;

public class CommandReflect {
	private int state;
	public CommandReflect(int in) {
		state = in;
	}
	public int addOne(Integer one) {
		return state + one.intValue();
	}
	public int addTwo(Integer one, Integer two) {
		return state + one.intValue() + two.intValue();
	}



	public static void main(String[] args) {
		CommandReflect[] objs = {new CommandReflect(1), new CommandReflect(2)};
		
		
		
	
		
		System.out.print("Normal call results: ");
		System.out.print(objs[0].addOne(new Integer(3)) + " ");
		System.out.print(objs[1].addTwo(new Integer(4), new Integer(5)) + " ");
		Command[] cmds = {
				new Command(objs[0], "addOne", new Integer[]{new Integer(3)}),
				new Command(objs[1], "addTwo", new Integer[]{new Integer(4),
						new Integer(5)})};
		System.out.print("\nReflection results:  ");
		for (int i = 0; i < cmds.length; i++)
			System.out.print(cmds[i].execute() + " ");
		System.out.println();
	}
}