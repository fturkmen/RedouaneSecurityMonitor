package org.workflow.xacml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.WritingException;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.api.PolicyRetrievalPoint;
import org.herasaf.xacml.core.api.UnorderedPolicyRepository;
import org.herasaf.xacml.core.context.RequestMarshaller;
import org.herasaf.xacml.core.context.ResponseMarshaller;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.simplePDP.SimplePDPConfiguration;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Redouane
 */
public class ControlPDP {
    
    public PDP simplePDP;
    public String pdpname;
    private File f = new File("C:/IIB002Request2.xml");
    private File P;// = new File("C:/Users/Redouan/workspace/herasaf-herasaf-xacml-core-2/XACMLPolicy/PolicyT1_canexecute.xml");
    //private File R;// = new File("C:/Users/Redouan/workspace/herasaf-herasaf-xacml-core-2/XACMLPolicy/RequestU1_T1.xml");
    private File R = new File("C:/Users/Redouan/workspace/herasaf-herasaf-xacml-core-2/XACMLPolicy/Request.xml");
    List<Evaluatable> policies;
    
    public void setPolicy(String policyFile){
    	P = new File(policyFile);
    }
    public void pdpInit(){
    	System.out.println("STARTING PDP");
    	pdpInitMethodSimple();    	
    	//
    	
    	//unmarshall the request
    	//RequestType request= unmarshalRequest(R);    	
    	//Evaluate the request against the policy
    	//ResponseType response = evaluateRequest(request);
    	
    	//result of the response
    	//String answer = repostas(response);
    	//System.out.println(answer);
    }
    
    public void setPdp(String pdpname){
    	this.pdpname = pdpname;
    	
    }
    
    public String getPdp(){
    	return pdpname;
    }
    
    //method is called by the pep
    public boolean DecisionRequest(String task, String user){
    	//P = new File("C:/Users/Redouan/workspace/herasaf-herasaf-xacml-core-2/XACMLPolicy/Policy"+task+"_canexecute.xml");
    	//P = new File("C:/Users/Redouan/workspace/herasaf-herasaf-xacml-core-2/XACMLPolicy/Policyt4_canexecute.xml");
    	//R = new File("C:/Users/Redouan/workspace/herasaf-herasaf-xacml-core-2/XACMLPolicy/Request"+user+"_"+task+".xml");
    	
    	deployPolicy(unmarshalPolicy(P));
    	//create XACML request
    	makeRequest(task, user);
    	//unmarshall the request
    	RequestType request= unmarshalRequest(R);    
    	
    	//Evaluate the request against the policy
    	ResponseType response = evaluateRequest(request);
    	
    	//result of the response
    	boolean answer = repostas(response);
    	//System.out.println("anwer is " +answer);
    	undeployPolicy(unmarshalPolicy(P));
    	return answer;
    }
    public void pdpInitMethodSimple() {
        /**
         * Creates a simple PDP with a default root combining algorithm and a
         * default policy repository. No PIP is used.
         */
        simplePDP = SimplePDPFactory.getSimplePDP();
        System.out.println("PDP has been started up and is running ....WAITING FOR REQUESTS");
    }
    
    public void pdpInitMethodCustom() {
        SimplePDPConfiguration config = new SimplePDPConfiguration();        
        simplePDP = SimplePDPFactory.getSimplePDP(config);
    }
    
    public void makeRequest(String task, String user) {
        
       // System.out.println(R.canRead());
        
        
        String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>                                 "
                + "<Request"
                + "   xmlns=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\">                  "        
                
                
                + "     <Subject>                                                                  "
                + "       <Attribute                           "
                + "        AttributeId=\"gfipm:2.0:user:username\"         "
                + "        DataType=\"http://www.w3.org/2001/XMLSchema#string\">                "
                + "        <AttributeValue>" + user + "</AttributeValue>                       "
                + "         </Attribute>                                                                   "
                + "        </Subject>                                       "
                + "             <Resource>                                                  "
                + "          <Attribute                                                 "
                + "          AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\"                                        "
                + "           DataType=\"http://www.w3.org/2001/XMLSchema#string\">                                                               "
                + "            <AttributeValue>" + task + "</AttributeValue>                                                       "
                + "            </Attribute>                                                           "
                + "          <Attribute                                                 "
                + "          AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:can_execute\"                                        "
                + "           DataType=\"http://www.w3.org/2001/XMLSchema#string\">                                                               "
                + "            <AttributeValue>" + user + "</AttributeValue>                                                       "
                + "            </Attribute>                                                           "
                + "             </Resource>                                             "
                + "              <Action>                 "
                + "               <Attribute                                    "
                + "             AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\"                                             "
                + "              DataType=\"http://www.w3.org/2001/XMLSchema#string\">                                                     "
                + "             <AttributeValue>execute</AttributeValue>                                                          "
                + "            </Attribute>                                            "
                + "       </Action>"
                + "          <Environment/>"
                + "          </Request>                            "
                + ""
                + ""
                + ""
                + "";
        
        byte[] b = msg.getBytes();
        
        try {
            FileOutputStream fil = new FileOutputStream(R);
            try {
                fil.write(b);
            } catch (IOException ex) {
                
            }    
            
        } catch (FileNotFoundException ex) {
            
        }
        
    }
    //deploy the policy in the PDP repository
    public void deployPolicy(Evaluatable policy) {

        /**
         * Retrieve the policy repository and deploy the policy on it.
         */
        PolicyRetrievalPoint repo = simplePDP.getPolicyRepository();
        UnorderedPolicyRepository repository = (UnorderedPolicyRepository) repo; //An OrderedPolicyRepository could be used as well
        repository.deploy(policy);
        
    }
    
    public void deployPolicies(List<Evaluatable> policies) {
        /**
         * Deploy mulitple policies on the policy repository.
         */
        PolicyRetrievalPoint repo = simplePDP.getPolicyRepository();
        UnorderedPolicyRepository repository = (UnorderedPolicyRepository) repo; //An OrderedPolicyRepository could be used as well
        repository.deploy(policies);
    }
    
    public void addPolicy(){
    	policies = new LinkedList<Evaluatable>();
    }
    
    public void undeployPolicy(Evaluatable policy) {

        /**
         * Retrieve the policy repository and undeploy the policy by its ID.
         */
        PolicyRepository repo = (PolicyRepository) simplePDP.getPolicyRepository();
        repo.undeploy(policy.getId());
        
    }
    
    public void undeployPolicies(List<EvaluatableID> policyIds) {
        /**
         * Undeploy multiple policies on the policy repository.
         */
        PolicyRepository repo = (PolicyRepository) simplePDP.getPolicyRepository();
        repo.undeploy(policyIds);
    }
    
    public ResponseType evaluateRequest(RequestType request) {

        /**
         * Evaluate the request using the simplePDP and retrieve the response
         * from the PDP.
         */
        ResponseType response = simplePDP.evaluate(request);
        return response;
        
    }
  //The XML policy is marshalled into a readable text
    public void marshalPolicy(Evaluatable policy) throws Exception {
        
        PolicyMarshaller.marshal(policy, System.out);
        
    }
  //The XML Request is marshalled into a readable text
    public void marshalRequest(RequestType request) throws Exception {
        
        RequestMarshaller.marshal(request, System.out);
        
    }
  //The XML Request is unmarshalled into a corresponding data structure that can be handled by the PDP
    public RequestType unmarshalRequest(File arquivoq) {
         RequestType requestType = null;
        try {
            requestType = RequestMarshaller.unmarshal(arquivoq);
        } catch (SyntaxException ex) {
            System.out.println("SADSADDDDDQWDQWDQWDQWDQWQW");
            Logger.getLogger(ControlPDP.class.getName()).log(Level.SEVERE, null, ex);
        }
       
      
       return requestType;
        
        
    }
    //The response is marshalled into a readable .txt file. 
    public boolean repostas(ResponseType r) {
        File file = new File("C:/Users/Redouan/workspace/herasaf-herasaf-xacml-core-2/XACMLPolicy/response.txt");
        
        boolean contains;
        String answer = null;    
        Writer writer = new StringWriter();
        try {
            ResponseMarshaller.marshal(r, file);
            answer = ResponseMarshaller.marshall(r, writer);
            //System.out.println("answer inside " +answer);
        } catch (WritingException ex) {
            System.out.println(ex.getMessage());
        }
        
        contains = (answer.contains("<Decision>Permit</Decision>") || answer.contains("<Decision>NotApplicable</Decision>"));
        return contains;        
    }
    
    //The XML policy is unmarshalled into a corresponding data structure that can be handled by the PDP
    public Evaluatable unmarshalPolicy(File f) {
        Evaluatable eval = null;
        try {
            eval = PolicyMarshaller.unmarshal(f);
        } catch (SyntaxException ex) {
            System.out.println("Método unmarshallPolicy da classe MyClass");
            Logger.getLogger(ControlPDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eval;               
    }
}

