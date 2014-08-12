package application.utils;

import application.FlowController;
import application.Source;
import org.controlsfx.dialog.Dialogs;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;

public class CompileCode {
    static int counter = 0;

    public static Object compileCode(Source source) {
        Object instance = null;
        String className = "SDEClass" + source.getId() + "C" + counter;
        try {
            // Prepare source somehow.
            String referenceID = FlowController.getFlowControllerFromSource(source).getReferenceID();
            String sourceString = "package programs;" +
                    "import application.utils.SDEUtils;" +
                    "import application.utils.DataBank;" +
                    "import application.utils.ScreenShotClass;" +
                    "import application.Program;" +
                    "import application.net.SSHManager;" +
                    "public class " + className + " implements Runnable {" +
                    " private String referenceID = \"" + referenceID + "\";" +
                    "  " + source.getSource() + "" +
                    "   private void save(String name, Object object) {" +
                    "      DataBank.saveVariable(name, object, this.referenceID);" +
                    "   }" +
                    "   private Object load(String name) {" +
                    "      return DataBank.loadVariable(name, this.referenceID);" +
                    "   }" +
                    "   private void run(String name) {" +
                    "      Program.runHelper(name, this.referenceID);" +
                    "   }" +
                    "}";

            String userHome = System.getProperty("user.home");

            //File dir = new File("C:\\developers\\alex\\svnwork\\focal-v6-demo-test\\SDE\\out\\production\\SDE\\programs");
            File dir = new File(userHome, "/SDE/programs");
            if (dir.exists()) {
                for (File file : dir.listFiles()) file.delete();
            }

            // Save source in .java file.
            File root = new File(userHome, "/SDE"); // On Windows running on C:\, this is C:\java.
            //File root = new File("C:\\developers\\alex\\svnwork\\focal-v6-demo-test\\SDE\\out\\production\\SDE\\programs"); // On Windows running on C:\, this is C:\java.
            File sourceFile = new File(root, "programs/" + className + ".java");
            sourceFile.getParentFile().mkdirs();

            new FileWriter(sourceFile).append(sourceString).close();
            // Compile source file.
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream err = new ByteArrayOutputStream();

            compiler.run(null, out, err, sourceFile.getPath());

            String outString = new String(out.toByteArray(), Charset.defaultCharset());
            String errString = new String(err.toByteArray(), Charset.defaultCharset());

            if (errString.length() > 1) {
                String lineNumber = errString.substring(errString.indexOf(className) + className.length() + 6);
                lineNumber = lineNumber.substring(0, lineNumber.indexOf(":"));
                Dialogs.create()
                        .owner(null)
                        .title("Compile error on " + source.getParentFlowNode().getContainedText())
                        .masthead("Error at line " + lineNumber)
                        .message(errString)
                        .showError();
            } else {
                // Load and instantiate compiled class.
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
                Class<?> cls = Class.forName("programs." + className, true, classLoader);
                instance = cls.newInstance();

                System.out.println("Compiled " + instance);
            }
            if (outString.length() > 1) {
                Dialogs.create()
                        .owner(null)
                        .title("ERR")
                        .masthead(null)
                        .message(outString)
                        .showError();
            }
        } catch (Exception ex) {
            Dialogs.create()
                    .owner(null)
                    .title("You do want dialogs right?")
                    .masthead(null)
                    .message("I was a bit worried that you might not want them, so I wanted to double check.")
                    .showException(ex);
        }
        counter++;
        return instance;
    }
}
