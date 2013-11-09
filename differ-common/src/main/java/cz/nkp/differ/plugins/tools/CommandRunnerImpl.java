package cz.nkp.differ.plugins.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author xrosecky
 */
public class CommandRunnerImpl implements CommandRunner {

    Logger logger = LogManager.getLogger(CommandRunner.class);


    @Override
    public CommandRunner.CommandOutput runCommandAndWaitForExit(File workingDir, List<String> arguments) throws IOException, InterruptedException {
	File binary = new File(arguments.get(0));
	if (!binary.exists()) {
	    logger.error("Binary {} does not exists, fix your configuration!", binary.getAbsolutePath());
	    return createErrorCommandOutput();
	}
	if (!binary.canExecute()) {
	    logger.error("Binary {} can't be executed, fix your configuration!", binary.getAbsolutePath());
	    return createErrorCommandOutput();
	}
	ProcessBuilder pb = new ProcessBuilder(arguments);
	pb.directory(workingDir);
	Process process = pb.start();
	ByteArrayOutputStream stderr = new ByteArrayOutputStream();
	ByteArrayOutputStream stdout = new ByteArrayOutputStream();
	CommandRunnerImpl.StreamGobbler sgerr = new CommandRunnerImpl.StreamGobbler(process.getErrorStream(), stderr);
	CommandRunnerImpl.StreamGobbler sgout = new CommandRunnerImpl.StreamGobbler(process.getInputStream(), stdout);
	sgerr.start();
	sgout.start();
	int exitCode = process.waitFor();
	sgerr.join();
	sgout.join();
	CommandRunner.CommandOutput result = new CommandRunner.CommandOutput(stdout.toByteArray(), stderr.toByteArray());
	result.setExitCode(exitCode);
	return result;
    }

    private CommandRunner.CommandOutput createErrorCommandOutput() {
	CommandRunner.CommandOutput result = new CommandRunner.CommandOutput(null, null);
	result.setExitCode(-1);
	return result;
    }

    public static class StreamGobbler extends Thread {

	InputStream is;
	OutputStream os;

	StreamGobbler(InputStream is, OutputStream os) {
	    this.is = is;
	    this.os = os;
	}

	@Override
	public void run() {
	    try {
		int c;
		while ((c = is.read()) != -1) {
		    os.write(c);
		    os.flush();
		}
	    } catch (IOException x) {
		throw new RuntimeException(x);
	    }
	}
    }
}
