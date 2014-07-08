package com.sparsity.sparksee.gsh;

import java.io.File;
import java.io.IOException;

import jline.History;

import org.codehaus.groovy.tools.shell.ExitNotification;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.codehaus.groovy.tools.shell.IO;
import org.codehaus.groovy.tools.shell.InteractiveShellRunner;

import com.sparsity.sparksee.groovy.MetaSparksee;

/**
 * 
 * @author sgomez
 *
 */
public class Shell {

    private static final File HISTORY_FILE = new File(
            System.getProperty("user.home") + "/.dexgsh_history");

    public Shell(IO io) {
        io.out.println("Welcome to the Dex groovy-based shell");
        io.out.println("-------------------------------------");

        Groovysh groovy = new Groovysh();
        ResultHandlerClosure rhc = new ResultHandlerClosure(groovy, io);
        rhc.showOutput(false);
        groovy.setResultHook(rhc);
        groovy.execute("import com.sparsity.sparksee.gdb.Sparksee;");
        groovy.execute("import com.sparsity.sparksee.*;");
        groovy.setHistory(new History());
        rhc.showOutput(true);

        PromptClosure pc = new PromptClosure(groovy);
        InteractiveShellRunner isr = new InteractiveShellRunner(groovy, pc);
        ErrorHandlerClosure ehc = new ErrorHandlerClosure(isr, io);
        isr.setErrorHandler(ehc);
        try {
            isr.setHistory(new History(HISTORY_FILE));
        } catch (IOException e1) {
            // continue with no history file :-(
        }

        MetaSparksee.ini();

        try {
            isr.run();
        } catch (ExitNotification e) {
        	System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Shell(new IO(System.in, System.out, System.err));
    }
}
