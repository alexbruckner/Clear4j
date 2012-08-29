package clear4j.config;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.processors.WorkflowProcessor;

@Config
public class Functions {

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

}
