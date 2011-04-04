package pcanvas.mesh;

import pcanvas.Point;
import pcanvas.Vector;
import pcanvas.draw.Colors;
import pcanvas.draw.CanvasPen3D;
import processing.core.PApplet;

import static pcanvas.Point.midPoint;
import static pcanvas.Vector.scaleVector;
import static processing.core.PApplet.TRIANGLES;

/**
 * NOTE: This is an experimental class, and should not be used until I say so. Kthxbai. =)
 */
public class MeshPen extends CanvasPen3D {

    private final Vector pointLabelDisplacement = new Vector(-4F, 4F, 12F);

    protected int vertexRadius = 5;

    protected float lightMode = 0;

    private Mesh mesh;

    private MeshPen(final PApplet applet, final Mesh mesh) {
        super(applet);
        this.mesh = mesh;
    }

    private void changeMesh(final Mesh mesh) {
        this.mesh = mesh;
    }

    private void drawMesh() {
        this.drawMesh(false);
    }

    private void drawMesh(final Boolean showLabels) {
        boolean showDistances = mesh.showDistances, showEdges = mesh.showEdges, showEB = mesh.showEB;
        boolean showTriangles = mesh.showTriangles, showSelectedTriangle = mesh.showSelectedTriangle;
        boolean showVertices = mesh.showVertices, showNormals = mesh.showNormals;
        int col=60;
        this.applet.noSmooth();
        this.applet.noStroke();
        
        if (showDistances) {
            showDistances();
        } else if (showEB) {
            showEB();
        } else if (showTriangles) {
            showTriangles();
        }

        if (showEdges) {
            this.setStrokeColor(Colors.BLUE);
            for(int i=0; i < mesh.numCorners; i++) {
                if(mesh.visible[mesh.cornerAsTriangle(i)]) {
                    drawEdge(i);
                }
            }

        }
        if (showSelectedTriangle) {
            this.applet.noStroke();
            this.setFillColor(Colors.GREEN);
            this.shade(mesh.cornerAsTriangle());
            this.applet.noFill();
        }

        this.setStrokeColor(Colors.RED);
        this.showBorder();

        if (showVertices) {
            this.applet.noStroke();
            this.applet.noSmooth();
            this.setFillColor(Colors.WHITE);
            for (int v=0; v<mesh.numVerts; v++) {
                this.drawPoint3D(mesh.G[v], pointRadius);
                this.applet.noFill();
            }
        }

        if (showNormals) {
            this.setStrokeColor(Colors.BLUE);
            this.showTriangleNormals();
            this.setStrokeColor(Colors.MAGENTA);
            this.showVertexNormals();
        }

        if (showLabels) {
            this.setFillColor(Colors.BLACK);
            Point toDraw;
            // draw labesl on vertices
            for (int i=0; i<mesh.numVerts; i++) {
                toDraw = mesh.G[i];
                this.drawPointLabel3D(toDraw, String.format("v%d", i), pointLabelDisplacement);
                //label("v"+str(i),labelD);
            }

            // draw labels on corners
            for (int i=0; i<mesh.numCorners; i++) {
                toDraw = mesh.cornerPoint(i);
                this.drawPointLabel3D(toDraw, String.format("c%d", i), pointLabelDisplacement);
                //.label("c"+str(i),labelD);
            }

            // draw labels on triangles
            for (int i=0; i<mesh.numTriangles; i++) {
                toDraw = mesh.triangleCenter(i);
                this.drawPointLabel3D(toDraw, String.format("t%d", i), pointLabelDisplacement);
                //label("t"+str(i),labelD);
            }
            this.applet.noFill();
        }
        this.applet.noStroke();
        this.setFillColor(Colors.DARK_RED);
        this.showCorner(mesh.previousCorner, pointRadius);
        this.setFillColor(Colors.DARK_BLUE);
        this.showCorner(mesh.currCorner, 2*pointRadius);
    }

    private void showDistances() {
        //for(int t=0; t<nt; t++) {if(Mt[t]==0) fill(cyan); else fill(ramp(Mt[t],rings)); shade(t);};
        for (int t = 0; t < mesh.numTriangles; t++) {
            if (mesh.triangleMarkers[t] == 0) {
                this.setFillColor(Colors.CYAN);
            } else {
                this.applet.fill(Colors.ramp(mesh.triangleMarkers[t], mesh.rings));
                this.shade(t);
            }
        }
    }

    private void showEB() {
        char[] triangleSymbols = mesh.triangleSymbol;
        for(int t=0; t<mesh.numTriangles; t++) {
            this.setFillColor(Colors.CYAN);
            if (triangleSymbols[t]=='w') {this.setFillColor(Colors.WHITE);}
            if (triangleSymbols[t]=='B') {this.setFillColor(Colors.BLACK);}
            if (triangleSymbols[t]=='C') {this.setFillColor(Colors.YELLOW);}
            if (triangleSymbols[t]=='L') {this.setFillColor(Colors.BLUE);}
            if (triangleSymbols[t]=='E') {this.setFillColor(Colors.MAGENTA);}
            if (triangleSymbols[t]=='R') {this.setFillColor(Colors.ORANGE);}
            if (triangleSymbols[t]=='S') {this.setFillColor(Colors.RED);}
            this.shade(t);
         }
    }

    private void showTriangles() {
        // if (lightingmode==0) fill(orange); else fill(white); for(int t=0; t<nt; t++)  shade(t); noFill();
        if (lightMode == 0) {
            this.setFillColor(Colors.ORANGE);
        } else {
            this.setFillColor(Colors.WHITE);
        }

        for (int t = 0; t < mesh.numTriangles; t++) {
            this.shade(t);
        }
        this.applet.noFill();
    }

    private void showTriangleNormals() {
        //for (int i=0; i<nt; i++) S(10*r,U(Nt[i])).show(triCenter(i));
        for (int i = 0; i < mesh.numTriangles; i++) {
            Vector normal =  scaleVector(mesh.triangleNormals[i].clone().normalize(), 10 * vertexRadius);
            this.drawVector(mesh.triangleCenter(i), normal);
        }
    }

    private void showVertexNormals() {
        //S(10*r,Nv[i]).show(G[i]);
        for (int i = 0; i < mesh.numVerts; i++) {
            Vector normal = scaleVector(mesh.vertexNormals[i], 10 * vertexRadius);
            this.drawVector(mesh.G[i], normal);
        }
    }

    private void showCorner(final int corner, final int radius) {
        Point cPt = midPoint(mesh.getVertex(corner), midPoint(mesh.getVertex(corner), mesh.cornerPoint(corner)));
        this.drawPoint3D(cPt, radius);
    }

    private void showCorner(final int corner, final float radius) {
        Point cPt = midPoint(mesh.getVertex(corner), midPoint(mesh.getVertex(corner), mesh.cornerPoint(corner)));
        this.drawPoint3D(cPt, radius);
    }

    private void showBorder() {
        for (int i=0; i< mesh.numCorners; i++) {
            if (mesh.visible[mesh.cornerAsTriangle(i)] && mesh.isBorder(i)) {
                drawEdge(i);
            }
        }
    }


    // draws edge of t(c) opposite to corner c
    private void drawEdge(int c) {
        this.drawLineSegments(mesh.getVertex(mesh.previousCorner(c)),mesh.getVertex(mesh.nextCorner(c)));
    }

    // shade triangles
    private void shade(int t) {
        if(mesh.visible[t]) {
            this.applet.beginShape(TRIANGLES);
            this.plotPoint(mesh.getVertex(3*t));
            this.plotPoint(mesh.getVertex(3*t+1));
            this.plotPoint(mesh.getVertex(3*t+2));
            this.applet.endShape();
        }
    }
}
