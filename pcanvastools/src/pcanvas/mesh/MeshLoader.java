package pcanvas.mesh;

import javax.swing.JFileChooser;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import pcanvas.Point;
import processing.core.PApplet;

import static processing.core.PApplet.loadStrings;

/**
 * NOTE: This is an experimental class, and should not be used until I say so. Kthxbai. =)
 */
public class MeshLoader {

    protected static void LoadMesh(final String meshName, final Mesh mesh) {
        System.out.println(String.format("Attempting to load mesh: '%s'", meshName));
        String targetName = meshName;

        if (meshName.split(".").length == 0) {
            targetName += ".vts";
        }

        LoadMesh(new File(targetName), mesh);
    }

    protected static void LoadMesh(final File file, final Mesh mesh) {
        try {
            String[] contents = loadStrings(file);
            int size = 0, numVerts = Integer.parseInt(contents[size++]);
            int numTriangles, numCorners;
            int a, b, c, comma1, comma2; float x, y, z;
            mesh.numVerts = numVerts;

            for (int k = 0; k < numVerts; k++) {
                int i = k + size;
                comma1 = contents[i].indexOf(',');
                x = Float.parseFloat(contents[i].substring(0, comma1));
                String rest = contents[i].substring(comma1 + 1, contents[i].length());
                comma2 = rest.indexOf(",");
                y = Float.parseFloat(rest.substring(0, comma2));
                z = Float.parseFloat(rest.substring(comma2 + 1, rest.length()));
                mesh.G[k].set(x, y, z);
            }
            size = numVerts + 1;
            numTriangles = Integer.parseInt(contents[size]);
            numCorners = 3 * numTriangles;
            mesh.numTriangles = numTriangles;
            mesh.numCorners = numCorners;
            
            size++;
            for (int k = 0; k < numTriangles; k++) {
                int i = k + size;
                comma1 = contents[i].indexOf(',');
                a = Integer.parseInt(contents[i].substring(0, comma1));
                String rest = contents[i].substring(comma1 + 1, contents[i].length());
                comma2 = rest.indexOf(',');
                b = Integer.parseInt(rest.substring(0, comma2));
                c = Integer.parseInt(rest.substring(comma2 + 1, rest.length()));
                mesh.vertexTable[3 * k] = a;
                mesh.vertexTable[3 * k + 1] = b;
                mesh.vertexTable[3 * k + 1] = c;
            }
        } catch (final Exception ex) {
            System.err.println(String.format("An error occurred while attempting to load mesh: '%s'.", file.getName()));
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    protected static void LoadMeshObj(final String meshName, final Mesh mesh) {
        System.out.println(String.format("Attempting to load mesh: '%s'", meshName));
        String targetName = meshName;

        if (meshName.split(".").length == 0) {
            targetName += ".obj";
        }

        LoadMeshObj(new File(targetName), mesh);
    }

    protected static void LoadMeshObj(final File file, final Mesh mesh) {
        //println("loading heart.obj");
        String [] ss = loadStrings(file);
        String subpts;
        String S;
        int comma1, comma2, a, b, c, s = 2;
        float x, y, z;
        int nn = ss[s].indexOf(':') + 2;
        int numVerts = Integer.parseInt(ss[s++].substring(nn));
        mesh.numVerts = numVerts;


        int k0=s;
        for(int k = 0; k < numVerts; k++) {
            int i=k+k0;
            S=ss[i].substring(2);
            //if(k == 0 || k == numVerts - 1) println(S);
            comma1=S.indexOf(' ');
            x = Float.parseFloat(S.substring(0, comma1));
            String rest = S.substring(comma1+1);
            comma2 = rest.indexOf(' ');
            y = Float.parseFloat(rest.substring(0, comma2));
            z = Float.parseFloat(rest.substring(comma2+1));
            mesh.G[k].set(x,y,z);
            if(k < 3 || k > numVerts - 4) {
                //print("k="+k+" : "); G[k].write();
            }
            s++;
        }
        s = s + 2;
        //println("Triangles");
        //println(ss[s]);
        nn = ss[s].indexOf(':')+2;
        int numTriangles = Integer.parseInt(ss[s].substring(nn)); 
        int numCorners = 3 * numTriangles;
        mesh.numTriangles = numTriangles;
        mesh.numCorners = numCorners;
        //println(", nt="+nt);
        s++;
        k0 = s;
        for(int k = 0; k < numTriangles; k++) {
            int i = k + k0;
            S=ss[i].substring(2);                        
            //if(k==0 || k==nt-1) println(S);
            comma1=S.indexOf(' ');   a=Integer.parseInt(S.substring(0, comma1));
            String rest = S.substring(comma1+1); comma2=rest.indexOf(' ');
            b=Integer.parseInt(rest.substring(0, comma2)); c=Integer.parseInt(rest.substring(comma2+1));
            mesh.vertexTable[3 * k] = a - 1;
            mesh.vertexTable[3 * k + 1] = b - 1;
            mesh.vertexTable[3 * k + 2] = c - 1;
        }
    }


    protected static void LoadMeshFromFile(final PApplet applet, final Mesh mesh) {
        final File dataDir = new File(applet.sketchPath, "data");        
        final JFileChooser chooser1 = new JFileChooser(dataDir);

        chooser1.showOpenDialog(applet);

        if (chooser1.getSelectedFile().getName().split(".").length == 1) {
            throw new IllegalArgumentException("Mesh files must have the extension \".vts\" or \".obj\"");
        } else {
            if (chooser1.getSelectedFile().getName().split(".")[1].equals("vts")) {
                LoadMesh(chooser1.getSelectedFile(), mesh);
            } else if (chooser1.getSelectedFile().getName().split(".")[1].equals("obj")) {
                LoadMeshObj(chooser1.getSelectedFile(), mesh);
            }
        }
    }

    protected static void SaveMesh(final String meshName, final Mesh mesh) {
        SaveMesh(meshName, mesh, false);
    }

    protected static void SaveMesh(final String meshName, final Mesh mesh, final Boolean flipOrientation) {
        Integer numVerts = mesh.numVerts, numTriangles = mesh.numTriangles;
        Point[] vertices = mesh.G;
        int[] vertexTable = mesh.vertexTable;

        // initialize array
        String [] data = new String [numVerts + 1 + numTriangles + 1];
        int s = 0;

        // what are we doing here?
        data[s++] = numVerts.toString();

        for (int i = 0; i < numVerts; i++) {
            data[s++] = String.format("%f, %f, %f", vertices[i].x, vertices[i].y,vertices[i].z);
        }

        data[s++] = numTriangles.toString();

        if (flipOrientation) {
            for (int i = 0; i < numTriangles; i++) {
                data[s++] = String.format("%d, %d, %d", vertexTable[3*i], vertexTable[3*i+2], vertexTable[3*i+1]);
            }
        } else {
            for (int i = 0; i < numTriangles; i++) {
                data[s++] = String.format("%d, %d, %d", vertexTable[3*i], vertexTable[3*i+1], vertexTable[3*i+2]);
            }
        }

        saveStrings(saveFile(meshName), data);
        //println("saved on file");
    }

    private static void saveStrings(final File file, final String[] strings) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out, 8192);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(bos, "UTF-8"));
            for (int i = 0; i < strings.length; i++) {
                writer.println(strings[i]);
            }
            writer.flush();
            writer.close();
        } catch (final IOException ioex) {
            System.err.println("Error occurred while attempting to save mesh strings.");
            System.err.println(ioex.getMessage());
            ioex.printStackTrace();
        }
    }

    private static File saveFile(String where) {
        // If the given filename is null, throw an exception.
        if (where == null) {
            throw new IllegalArgumentException("Attempted to save a mesh with a null filename.");
        }

        String[] filePath = where.split(File.separator);
        String[] fileName = filePath[filePath.length - 1].split(".");
        String fileDestination = "";

        // Check if the filename already has ".vts" as part of its name, if not, append it. If it has
        // some unknown extension, throw an exception.
        if (fileName.length == 1) {
            for (int i = 0; i < filePath.length; i++) {
                fileDestination += filePath[i] + File.separator;
            }
            fileDestination += ".vts";
        } else if (fileName.length > 1) {
            if (!fileName[1].equals("vts")) {
                throw new IllegalArgumentException("Mesh files must be saved in the \"filename.vts\" format.");
            }
        }
        return new File(assertSketchPath(fileDestination));
    }

    private static String assertSketchPath(final String where) {
        try {

            String[] filePath = where.split(File.separator);
            String[] dirs = new String[filePath.length - 1];
            String savePath = "";
            if (dirs.length > 0) {
                System.arraycopy(filePath, 0, dirs, 0, filePath.length - 1);
                savePath = inflateDirectories(dirs);
            } else {
                System.out.println("No save directory specificed. Saving mesh to 'data' folder.");
                savePath = inflateDirectories("data");
            }

            return savePath + filePath[filePath.length - 1];            

        } catch (final Exception ex) {
            System.err.println("An error occurred while attempting to save the mesh.");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    private static String inflateDirectories(final String ... filePath) {
        try {
            System.out.println("Inflating directories for save path...");
            String path = "";
            for (int i = 0; i < filePath.length; i++) {
                String dir = filePath[i];
                path += (i != filePath.length) ? (dir + File.separator) : dir;
            }
            File directory = new File(path);
            directory.mkdirs();
            return path;
        } catch (final Exception ex) {
            System.err.println("An error occurred while attempting to inflate directories for mesh file save.");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
