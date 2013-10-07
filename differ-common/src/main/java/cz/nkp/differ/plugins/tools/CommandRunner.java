package cz.nkp.differ.plugins.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface CommandRunner {
   
    public CommandRunner.CommandOutput runCommandAndWaitForExit(File workingDir, List<String> arguments) throws IOException, InterruptedException;

    public static class CommandOutput {
	private byte[] stdout;
	private byte[] stderr;
	private int exitCode = -1;

	public CommandOutput(byte[] stdout, byte[] stderr) {
	    this.stdout = stdout;
	    this.stderr = stderr;
	}

	public int getExitCode() {
	    return exitCode;
	}

	public void setExitCode(int exitCode) {
	    this.exitCode = exitCode;
	}

	public byte[] getStderr() {
	    return stderr;
	}

	public byte[] getStdout() {
	    return stdout;
	}
	
    }
}
