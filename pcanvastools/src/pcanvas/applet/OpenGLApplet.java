package pcanvas.applet;
/*
    ========== OpenGL Libraries ==========
    === GLU ===
    LINUX: libgluegen-rt.so
    OSX:   libgluegen-rt.jnilib
    WIN:   gluegen-rt.dll

    === OGL ===
    LINUX: libjogl_cg.so, libjogl_awt.so, libjogl.so
    OSX:   libjogl_cg.jnilib, libjogl_awt.jnilib, libjogl.jnilib
    WIN:   jogl_cg.dll, jogl_awt.dll, jogl.dll
 */

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import processing.core.PApplet;

/**
 * Custom applet used to extract native jogl and gluegen-rt libraries onto the local machine, then load them into
 * the {@link System} library path at runtime.
 * <br />
 * <br />
 * <strong>NOTE:</strong> This applet should only be extended if the extending applet calls:
 * {@link PApplet#size(int, int, String)} where the last parameter is {@link PApplet#OPENGL}.
 */
public class OpenGLApplet extends PApplet {

    private static String LIB_VER = "1.0";
    private static String LIB_NAME = String.format("PCanvasTools-%s", LIB_VER);

    /**
     * Overridden {@link PApplet#setup()} method. Extending applets need to call this in the first line of their
     * own setup methods in order for the extraction to occur properly.
     */
    @Override
    public void setup() {
        this.setupEnv();
    }

    /**
     * Fake setup method. This method is injected by the export script during applet export in order to force this
     * applet to realize that it is being run from a web-platform. Unfortunately due to java applet policies, we
     * are not allowed to retrieve system properites at runtime without requiring the user to have a .java.policy
     * file in their home directory.<br /><br />
     * This is dangerous because the user may not be familiar with the mentioned policy file(s), and it's a bit
     * annoying as a user to be asked to have to move a file somewhere just to get a piece of code to work. =/
     */
    public void setupWeb() {
        super.setup();
    }

    /**
     * Called from the {@link #setup()} method. This particluar method is what does all of the heavy lifting
     * during runtime before the applet loads.
     * <br />
     * <br />
     * In general, it attempts to locate the PCanvasTools-X.X.jar archive, extract the correct system libraries needed
     * for Processing to properly use its OpenGL bindings into a temporary directory, then load those libraries into
     * the java library path at runtime.
     */
    private void setupEnv() {        
        //System.out.println("Preparing environment.");
        try {
            String gluJarStr, joglJarStr;
            String os, library, appendPath;
            String arch = (System.getProperty("os.arch").equalsIgnoreCase("x86")) ? "i586" : "amd64";
            switch (PApplet.platform) {
                case WINDOWS:
                    os = "windows";
                    library = "%s.dll";
                    appendPath = String.format("file:/C:\\Temp\\%s", LIB_NAME);
                    break;
                case MACOSX:
                    os = "macosx";
                    arch = "universal";
                    library = "lib%s.jnilib";
                    appendPath = String.format("file:/tmp/%s", LIB_NAME);
                    break;
                case LINUX:
                    os = "linux";
                    library = "lib%s.so";
                    appendPath = String.format("file:/tmp/%s", LIB_NAME);
                    break;
                default:
                    throw new UnsupportedOperationException("Java OpenGL is no supported on this operating system.");
            }

            gluJarStr = String.format("natives/gluegen-rt-natives-%s-%s.jar", os, arch);
            joglJarStr = String.format("natives/jogl-natives-%s-%s.jar", os, arch);

            String baseDir = getClass().getProtectionDomain().getCodeSource().getLocation().toExternalForm().replace("%20", " ");
            String baseJarStr = String.format("%s%s.jar", baseDir, LIB_NAME);
            File f = new File(new URL(baseJarStr).getFile());

            // TODO: Abstract this functionality to a function so we can call it on multiple library dependencies.
            if (!f.exists()) {
                baseJarStr = String.format("%slib/%s.jar", baseDir, LIB_NAME);
                f = new File(new URL(baseJarStr).getFile());
                if (!f.exists()) {                    
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                    }
                    String err = String.format("Unable to locate %s.jar in root or 'lib' folder of the java classpath.\n"
                            + "Please make sure the library is in the root or 'lib' folder of the code being executed.",
                            LIB_NAME);
                    System.err.println(err);
                    JOptionPane.showMessageDialog(this, err, "Library Location Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }

            JarFile libJar = new JarFile(f);
            JarEntry gluNativesJar = libJar.getJarEntry(gluJarStr);
            JarEntry joglNativesJar = libJar.getJarEntry(joglJarStr);

            this.extractJars(libJar, gluNativesJar, appendPath);
            this.extractJars(libJar, joglNativesJar, appendPath);

            /* Get the gluegen-rt jars. */
            f = new File(new URL(appendPath + "/" + gluJarStr).getFile());
            JarFile gluJar = new JarFile(f);
            JarEntry gluegen = gluJar.getJarEntry(String.format(library, "gluegen-rt"));

            /* Get the jogl and jogl_awt jars. */
            f = new File(new URL(appendPath + "/" + joglJarStr).getFile());
            JarFile joglJar = new JarFile(f);
            JarEntry jogl = joglJar.getJarEntry(String.format(library, "jogl"));
            JarEntry jogl_awt = joglJar.getJarEntry(String.format(library, "jogl_awt"));

            /* Extract the natives. */
            this.extractNatives(gluJar, gluegen, appendPath);
            this.extractNatives(joglJar, jogl_awt, appendPath);
            this.extractNatives(joglJar, jogl, appendPath);

            this.modifyLibPath(appendPath + "/natives");

            System.loadLibrary("gluegen-rt");
            System.loadLibrary("jogl");
            System.loadLibrary("jogl_awt");

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Extracts an individual jar from the PCanvasTools-X.X.jar file into the users system temp directory.
     * This is necessary as we cannot extract from two levels deep from java.
     * @param parent the parent {@link JarFile} containing another {@link JarFile} (or {@link JarEntry}).
     * @param entry the {@link JarEntry} the represents the jar file needing extraction.
     * @param baseDir the base directory location for the parent jar file.
     */
    private void extractJars(final JarFile parent, final JarEntry entry, final String baseDir) {
        try {
            File f = new File(new URL(baseDir + "/natives").getFile());
            f.mkdirs();
            f = new File(new URL(baseDir+ "/" +  entry.getName()).getFile());
            if (f.exists()) {
                return;
            }
            InputStream is = parent.getInputStream(entry);
            FileOutputStream fos = new FileOutputStream(f);
            while (is.available() > 0) {
                fos.write(is.read());
            }
            fos.close();
            is.close();
        } catch (final IOException ex) {
            System.err.println("Error. =(");
            ex.printStackTrace();
        }
    }

    /**
     * Extracts the native library binaries ('.dll' for windows, '.so' for *nix and '.jnilib' for OS X) to the file
     * system from a parent jar.
     * @param parent the parent {@link JarFile} containing the native needing extraction.
     * @param entry the {@link JarEntry} the represents the native needing extraction.
     * @param baseDir the base directory location for the parent jar file.
     */
    private void extractNatives(final JarFile parent, final JarEntry entry, final String baseDir) {
        try {
            File f = new File(new URL(baseDir + "/natives").getFile());
            f.mkdirs();
            f = new File(new URL(baseDir+ "/natives/" +  entry.getName()).getFile());
            if (f.exists()) {
                return;
            }
            InputStream is = parent.getInputStream(entry);
            FileOutputStream fos = new FileOutputStream(f);
            while (is.available() > 0) {
                fos.write(is.read());
            }
            fos.close();
            is.close();
        } catch (final IOException ex) {
            System.err.println("Error. =(!");
            ex.printStackTrace();
        }
    }

    /**
     * Modifies the {@link System} library.path runtime setting to add our temp directory containing the natives.
     * This is completely necessary, as Processing desperately needs these libraries to access java OpenGL bindings.
     * @param location the location being added to the runtime library path.
     * @throws IOException if for some reason we are unable to properly modify the library path (permissions or incorrect field handles).
     */
    private void modifyLibPath(final String location) throws IOException {
        try {
            String appDir = location.replace("file:/", "");
            if (PApplet.platform == WINDOWS) {
                appDir = appDir.replace('/','\\');
            } else {
                appDir = "/" + appDir;
            }
            /*System.out.println("New Libdir: " + appDir);*/

            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            String[] paths = (String[])field.get(null);
            if (Arrays.asList(paths).contains(appDir)) return;
            String[] tmp = new String[paths.length+1];
            System.arraycopy(paths,0,tmp,0,paths.length);
            tmp[paths.length] = appDir;
            field.set(null, tmp);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }
}
