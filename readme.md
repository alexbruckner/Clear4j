<img src="http://dl.dropbox.com/u/21134784/Clear4j_trans.png"/>
=======
___

A Remote Workflow Framework for Java
===============================
---


About
-----

How it works
------------

Examples
--------

Reference Implementation
-----------------------


TODO
====
* clean up @Param and allow for unordered Param method signatures.
* exception test cases for missing method declarations with proper errors saying which ones are missing.
* move test functions into test
* each processor queue should handle messages specific to the processor rather than having a deal with workflow schema as currently done in WorkflowProcessor.
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
* graphical workflow definition and execution tool
* scripting language for workflow definition and execution
* hot deploying remote processors on remote machines from local

Notes
-----
cover image generated by: http://uk1.flamingtext.com

