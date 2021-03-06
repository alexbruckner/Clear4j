<img src="http://dl.dropbox.com/u/21134784/Clear4j_trans.png"/>
=======
___

A 'Clear Code' Remote Workflow Framework for Java
===============================
---


About
-----

This framework is still under development. The alpha release should be ready in around 6-12 months.

How it works
------------

Rather than pulling in dependencies though conventional means into a class,
you can write and sequence functions that run locally or remotely into a workflow and unix style pipe values between.
These workflow sequences can be forked and joined and there will be a monitoring page provided
in order to see what is going on behind the scenes. (i.e. which functions are executing on which server).
Concurrency is provided in that stateful calls are being messaged to stateless functions.

Examples
--------

<b>Setup:</b>

1. A config class to declare the available functions (and whether they are remote or local):

<pre>
<code>
@Config
public class Functions {

    // runs locally (load the contents of an example text file)
	public static Function loadText(){
		return new Function(FileProcessor.class, "loadText", String.class);
	}

    // runs on remote machine (prints piped value on to the console)
	public static Function println(){
		return new Function(new HostPort("remote.machine.com", 7777), PrintProcessor.class, "println", Object.class);
	}

</code>
</pre>

2. Implementation Processor classes:

<pre>
<code>
public class FileProcessor {

	@Function
	public String loadText(String path){
		return FileUtils.loadTextFromFile(path);
	}

}
</code>
</pre>

<pre>
<code>
public class PrintProcessor {

	@Function
	public Object println(Object value){
        System.out.println(value);
        return value;
	}

}
</code>
</pre>

3. Creating the workflow.

You can create a Workflows class that predefines workflows or create functions on the fly.

<pre>
<code>
public static Workflow loadTextAndPrintRemotely(String path){
    return new Workflow(path, Functions.loadText(), Functions.println());
}
</code>
</pre>

4. Running the workflow (with optional thread waiting on a remotely returned workflow).

<pre>
<code>
Clear.run(Workflows.loadTextAndPrintRemotely("path_to_text_file")).waitFor();
</code>
</pre>

This would load the text file in the local JVM and print them on the console in the remote JVM.




Reference Implementation
-----------------------

NY/A


TODO
====
* each processor queue should handle messages specific to the processor rather than having a deal with workflow schema as currently done in WorkflowProcessor.
* workflow to stay local - only make remote function calls?
* check whether finished remote functions update the instruction status correctly in the workflow. (works locally)
* fork-join support in workflow
* example blogsite. initially show workflow in controller that saves a blog entry. then make that workflow into one processor that is called.
* timeouts on waitFor. so that can wait on user input within workflow back from webserver processor?
* remove @SuppressWarnings("unchecked") for waitFor results (see webserver)
* named method parameters instead of or additionally to Args... / ie (@Param("key") Arg<String, T> value) {where value.name = "key"} ?
* cleaning up on how functions are defined. hot deploy?
* more on exception handling (exception queue?)
* extra parameter(s) per function needs cleaning up
* clear framework for creating workflow object, like Workflow.create(function).with(initialValue).pipeTo(anotherFunction).pipeTo(anotherFunction).run();
* load balancing of functions
* more example processors
* iterative constructs (ForEach processor, ie load file -> for each line, do...)?
* graphical workflow definition and execution tool
* scripting language for workflow definition and execution
* hot deploying remote processors on remote machines from local

Notes
-----
cover image generated by: http://uk1.flamingtext.com

