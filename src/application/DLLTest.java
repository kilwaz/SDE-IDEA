package application;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Simple example of Windows native library declaration and usage.
 */
public class DLLTest {
    public interface Kernel32 extends Library {
        // FREQUENCY is expressed in hertz and ranges from 37 to 32767
        // DURATION is expressed in milliseconds
        public boolean Beep(int FREQUENCY, int DURATION);

        public void Sleep(int DURATION);
    }

    public interface POSIX extends Library {
        public int chmod(String filename, int mode);

        public int chown(String filename, int user, int group);

        public int rename(String oldpath, String newpath);

        public int kill(int pid, int signal);

        public int link(String oldpath, String newpath);

        public int mkdir(String path, int mode);

        public int rmdir(String path);
    }

    public void test() {
        POSIX posix = (POSIX) Native.loadLibrary("c", POSIX.class);
        posix.mkdir("/tmp/newdir", 0777);
        posix.rename("/tmp/newdir", "/tmp/renamedir");

//        Kernel32 lib = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
//        lib.Beep(698, 500);
//        lib.Sleep(500);
//        lib.Beep(698, 500);
    }
}