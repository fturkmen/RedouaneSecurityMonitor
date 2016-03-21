package org.workflow.xacml;
import java.awt.BorderLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.BadLocationException;




public class MainFrame extends JFrame{
	private JTextArea textArea;
	private JButton constraint;
	private JButton policy;
	private JPanel jpanel;
	//private PrintStream standardOut;
	private JFileChooser chooser;
	private String constraintsPath = "C:/Users/Redouan/workspace/Constraints/constraints.txt";
	private String policyPath;
	public MainFrame(){
		super("Security Monitor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(600, 500);
        setLayout(new BorderLayout());
        // os = System.out;
        
        textArea = new JTextArea();
        jpanel = new JPanel();
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
       // standardOut = System.out;
        System.setOut(printStream);
        System.setErr(printStream);               
        constraint = new JButton("Constraints");
        policy = new JButton("Policy");
        constraint.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		selectConstraints();
        	}
        	
        });
        
        policy.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		selectPolicy();
        	}
        	
        });
        
        add(textArea, BorderLayout.CENTER); 
        add(jpanel, BorderLayout.SOUTH);
        jpanel.add(constraint, BorderLayout.SOUTH);
        jpanel.add(policy, BorderLayout.SOUTH);
	}
	
	public void decisionUpdate(String s){
		System.out.println(s);
	}
	
	public void selectConstraints(){
		JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File("C:/Users/Redouan/workspace/Constraints/constraints.txt"));
	    chooser.setDialogTitle("Select constraints");
	    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    chooser.setAcceptAllFileFilterUsed(false);

	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	      //System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
	      //System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
	      constraintsPath = chooser.getSelectedFile().getAbsolutePath();
	      //System.out.println("Pad is "+constraintsPath);
	    } else {
	      System.out.println("No Selection ");
	    }
	}
	
	public void selectPolicy(){
		JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Select constraints");
	    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    chooser.setAcceptAllFileFilterUsed(false);

	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	      //System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
	      //System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
	      policyPath = chooser.getSelectedFile().getAbsolutePath();
	      //System.out.println("Pad is "+constraintsPath);
	    } else {
	      System.out.println("No Selection ");
	    }
	}
	
	public String getConstraint(){
		return constraintsPath;
	}
	
	public String getPolicy(){
		return policyPath;
	}
}
