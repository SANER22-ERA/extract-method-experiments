package duplicatesextractor.extractMethod;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NonNls;
import duplicatesextractor.extractMethod.core.extractors.PositivesExtractor;
import duplicatesextractor.extractMethod.core.extractors.RefactoringsExtractor;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Plugin starter for extraction of negative refactorings
 */
public class PositivesRunner extends BaseRunner {
    @Override
    public @NonNls
    String getCommandName() {
        return "PositiveRefactorings";
    }

    @Override
    Options configureOptionsForCLI() {
        Options options = new Options();
        options.addRequiredOption("paths", "projectsDirPath",
                true, "Path to the directory containing the projects.");
        options.addRequiredOption("out", "outputFilePath",
                true, "Path to the desired output destination.");

        return options;
    }

    @Override
    void extractRefactorings(CommandLine cmdLine) {
        ExtractionRunner runner = new ExtractionRunner();
        try {
            RunnerUtils.configureOutput(cmdLine);
        } catch (MissingArgumentException e) {
            LOG.error("<datasetsDirPath> is a required argument.");
            return;
        } catch (IOException e) {
            LOG.error("Failed to create output directory.");
        }

        Path inputDir = null;
        try {
            inputDir = Paths.get(cmdLine.getOptionValue("projectsDirPath"));
        } catch (InvalidPathException e) {
            LOG.error("<projectsDirPath> has to be a valid path.");
            return;
        }


        FileWriter positiveFW = null;
        try {
            positiveFW = RunnerUtils.makePositiveHeader(cmdLine.getOptionValue("outputFilePath"), featureCount);
        } catch (IOException e) {
            LOG.error("Failed to make header for the output file");
            return;
        }

        RefactoringsExtractor extractor = new PositivesExtractor(positiveFW);

        try {
            runner.runMultipleExtractions(inputDir, extractor);
        } catch (Exception e) {
            LOG.error("Unexpected error in positive" +
                    " samples' procedure. \n" + e.getMessage());
        }
    }
}
