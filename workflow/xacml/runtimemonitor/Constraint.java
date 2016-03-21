package org.workflow.xacml.runtimemonitor;

import org.workflow.xacml.Pep;

public interface Constraint {
	public boolean evaluate(String task, String user);

}
