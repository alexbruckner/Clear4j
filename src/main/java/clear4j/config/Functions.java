package clear4j.config;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.processors.FileProcessor;
import clear4j.processors.PrintProcessor;
import clear4j.processors.SleepProcessor;
import clear4j.processors.WorkflowProcessor;

@Config
public class Functions {

	public static Function loadText() {
		return new Function(FileProcessor.class, "loadText", String.class);
	}

	public static Function println() {
		return new Function(PrintProcessor.class, "println", Object.class);
	}

	/*
		 * SPECIALS
		 */

	public static Function initialProcess() {
		return new Function(WorkflowProcessor.class, "initialProcess", Workflow.class);
	}

	public static Function finalProcess() {
		return new Function(WorkflowProcessor.class, "finalProcess", Workflow.class);
	}

	/*
		 * MORE SPECIALS
		 */

	public static Function monitor() {
		return new Function(WorkflowProcessor.class, "monitor", null);
	}

	public static Function sleep() {
		return new Function(SleepProcessor.class, "sleep", Object.class);
	}

}
