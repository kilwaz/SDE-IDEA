package application.utils;

import application.FlowController;
import application.Source;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

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
                    "   private SSHManager ssh(String connectionIP, String username, String password) {" +
                    "      return SDEUtils.openSSHSession(connectionIP, username, password);" +
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
            compiler.run(null, null, null, sourceFile.getPath());

            // Load and instantiate compiled class.
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
            Class<?> cls = Class.forName("programs." + className, true, classLoader);
            instance = cls.newInstance();

            System.out.println("Compiled " + instance);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        counter++;
        return instance;
    }
}
