package fr.upem.foraxproof.main;

import fr.upem.foraxproof.core.ForaxProof;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

class CommandLineParser {
    static class Option {
        String inputJar;
        HashSet<String> inputClasses = new HashSet<>();
        String inputUrl;
        HashSet<String> rules = new HashSet<>();
        String exporter;
        String mode;
        int thread;
        String zip;
        boolean server = false;
        boolean allRules = false;

        public Option() {}

        @Override
        public String toString() {
            return "Options {" +
                    "\n\tinputJar = " + inputJar +
                    "\n\tinputClasses = " + inputClasses +
                    "\n\tinputUrl = " + inputUrl +
                    "\n\trules = " + (allRules ? "ALL" : rules) +
                    "\n\texporter = " + exporter +
                    "\n\tmode = " + mode +
                    "\n\tthread = " + thread +
                    "\n\tzip = " + zip +
                    "\n\tserver = " + server +
                    "\n}";
        }
    }

    static Option parseArguments(String [] args) {
        Option option = new Option();
        HashMap<String, Consumer<Iterator<String>>> actions = new HashMap<>();
        populateActions(actions, option);
        for (Iterator<String> it = List.of(args).iterator(); it.hasNext();) {
            parse(it, actions);
        }
        validate(option);
        defaultConfig(option);
        return option;
    }

    static String getHelpMessage(){
        return "Usage: "
                + "\n\t-j <PATH_TO_JARFILE>\t\tSpecify a JAR file. Parses all classes in the JAR."
                + "\n\t-r <RULE_NAME>\t\t\t\tSpecify a rule to use. Can be used multiple times."
                + "\n\t-c <CLASS_PATH>\t\t\t\tSpecify a class to analyse with the program. Can be used multiple times."
                + "\n\t-d <PATH_TO_DIRECTORY>\t\tSpecify a directory. Parses all classes, recursively, in the directory."
                + "\n\t-u <URL>\t\tSpecify an url of jar to analyse."
                + "\n\t-s\t\t\t\t\t\t\tOption used to launch the SpringBoot server."
                + "\n\t-m <MODE_NAME>\t\tSpecify the mode to use ['concurrent' or 'default']"
                + "\n\t-e <EXPORTER_NAME>\t\tSpecify the exporter to use ['xml', 'sql' or 'print']"
                + "\n\t-t <NB_THREAD>\t\tSpecify the number of thread to use. Only available in concurrent mode."
                + "\n\t-z <PATH_TO_SOURCE_ZIP>\t\tSpecify a source ZIP file. Allow to have source extract inside vuejs."
                + "\n\t-h\t\t\t\t\t\t\tDisplay this help message."
                + "\nExamples:"
                + "\n\tjava Main.java -d .\t\t\t To parse the project directory."
                + "\n\tjava Main.java -s -c ExampleClass.class -c ExampleClass2.class"
                + "\n\tjava Main.java -c ExampleClass.class -j ExampleJar.jar -o Output.xml";
    }

    @ForaxProof("Impossible to reduce this, populateActions is logic")
    private static void populateActions(HashMap<String, Consumer<Iterator<String>>> actions, Option option) {
        actions.put("-j", it -> option.inputJar = parseString(it, "-j : Jar option requires an argument"));
        actions.put("-c", it -> option.inputClasses = parseStrings(it, "-c : Class option requires an argument"));
        actions.put("-u", it -> option.inputUrl = parseString(it, "-u : URL option requires an argument"));
        actions.put("-r", it -> option.rules = parseStrings(it, "-r : Rule option requires an argument"));
        actions.put("-d", it -> parseDirectory(it, option));
        actions.put("-e", it -> option.exporter = parseString(it, "-e : Exporter option requires an argument"));
        actions.put("-m", it -> option.mode = parseString(it, "-m : Mode option requires an argument"));
        actions.put("-z", it -> option.zip = parseString(it, "-z : ZIP option requires an argument"));
        actions.put("-t", it -> option.thread = parseInt(it, "-t : Thread option requires an argument"));
        actions.put("-s", it -> option.server = true);
        actions.put("-h", it -> System.out.println(getHelpMessage()));
    }

    private static void parse(Iterator<String> it, HashMap<String, Consumer<Iterator<String>>> actions) {
        String cmd = it.next();
        Consumer<Iterator<String>> action = actions.get(cmd);
        if (action == null) {
            throw new IllegalArgumentException("Unexpected token " + cmd);
        }
        action.accept(it);
    }

    private static void validate(Option option) {
        if (option.inputClasses.isEmpty() && option.inputJar == null && option.inputUrl == null) {
            throw new IllegalArgumentException("You must specify a -j, -c, -d or -u option");
        }
    }

    private static void defaultConfig(Option option) {
        if (option.rules.isEmpty()) {
            option.allRules = true;
        }
        option.exporter = option.exporter == null ? "print" : option.exporter;
        option.mode = option.mode == null ? "default" : option.mode;
        option.thread = option.thread < 1 ? 200 : option.thread;
    }

    @ForaxProof("Impossible to reduce this, we use a stream in this method")
    private static void parseDirectory(Iterator<String> it, Option option) {
        if (!it.hasNext()) {
            throw new IllegalArgumentException("-d : Directory option requires an argument");
        }
        try {
            Files.walk(Paths.get(it.next()))
                    .filter(Files::isRegularFile)
                    .filter(f -> f.toString().endsWith(".class"))
                    .map(Path::toString)
                    .forEach(path -> option.inputClasses.add(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String parseString(Iterator<String> it, String desc) {
        if (!it.hasNext()) {
            throw new IllegalArgumentException(desc);
        }
        return it.next();
    }

    private static HashSet<String> parseStrings(Iterator<String> it, String desc) {
        if (!it.hasNext()) {
            throw new IllegalArgumentException(desc);
        }
        String[] str = it.next().split(",");
        HashSet<String> set = new HashSet<>();
        Collections.addAll(set, str);
        return set;
    }

    private static int parseInt(Iterator<String> it, String desc) {
        if (!it.hasNext()) {
            throw new IllegalArgumentException(desc);
        }
        return Integer.parseInt(it.next());
    }
}
