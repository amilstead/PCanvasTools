package pcanvas.legacy;

import static processing.core.PApplet.abs;
import static processing.core.PApplet.atan2;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.sq;
import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.PI;
import static processing.core.PConstants.TWO_PI;

/**
 * Legacy functions for manipulating pt and vec objects. Courtesy of Jarek Rossignac.
 */
public class Functions {
    /* Point creation functions. */

    public static pt P(final float x, final float y) {
        return new pt(x, y, 0.0F);
    }

    public static pt P(final pt pt) {
        return new pt(pt.x, pt.y, pt.z);
    }

    public static pt P() {
        return P(0, 0);
    }

    /* vec creation functions. */
    public static vec V(final float x, final float y) {
        return new vec(x, y, 0.0F);
    }

    public static vec V(final vec v) {
        return new vec(v.x, v.y, v.z);
    }

    public static vec V(final pt P, final pt Q) {
        return new vec(Q.x - P.x, Q.y - P.y, Q.z - P.z);
    }

    // averages and linear interpolations of points
    public static pt L(pt A, float s, pt B) {return P(A.x+s*(B.x-A.x),A.y+s*(B.y-A.y)); }                             // L(A,s,B): A+sAB (linear interpolation between points)
    public static pt A(pt A, pt B) {return P((A.x+B.x)/2.0F,(A.y+B.y)/2.0F); }                                          // A(A,B): (A+B)/2 (average)
    public static pt A(pt A, pt B, pt C) {return P((A.x+B.x+C.x)/3.0F,(A.y+B.y+C.y)/3.0F); }                            // A(A,B,C): (A+B+C)/3 (average)
    // weighted sums of points
    public static pt S(pt A, pt B) {return new pt(A.x+B.x,A.y+B.y); }                                                 // S(A,B): A+B
    public static pt S(pt A, pt B, pt C) {return S(A,S(B,C)); }                                                       // S(A,B,C): A+B+C
    public static pt S(pt A, pt B, pt C, pt D) {return S(S(A,B),S(C,D)); }                                            // S(A,B,C,D): A+B+C+D
    public static pt S(pt A, float s, pt B) {return S(A,S(s,B)); }                                                    // S(A,s,B): A+sB (used in summations of center of mass and in circle inversion)
    public static pt S(float a, pt A, float b, pt B) {return S(S(a,A),S(b,B));}                                        // S(a,A,b,B): aA+bB
    public static pt S(float a, pt A, float b, pt B, float c, pt C) {return S(S(a,A),S(b,B),S(c,C));}                  // S(a,A,b,B,c,C): aA+bB+cC
    public static pt S(float a, pt A, float b, pt B, float c, pt C, float d, pt D){return A(S(a,A,b,B),S(c,C,d,D));}   // S(a,A,b,B,c,C,d,D): aA+bB+cC+dD (used in smoothing and subdivision)
    // combinations of vectors
    public static vec S(vec U, vec V) {return new vec(U.x+V.x,U.y+V.y);}                                                // S(U,V): U+V
    public static vec S(float s,vec V) {return new vec(s*V.x,s*V.y);}                                                  // S(s,V): sV
    public static vec S(float a, vec U, float b, vec V) {return S(S(a,U),S(b,V));}                                      // S(a,U,b,V): aU+bV )Linear combination)
    public static vec S(vec U,float s,vec V) {return new vec(U.x+s*V.x,U.y+s*V.y);}                                    // S(U,s,V): U+sV
    public static vec A(vec U, vec V) {return new vec((U.x+V.x)/2.0F,(U.y+V.y)/2.0F); }                                  // A(U,V): (U+V)/2 (average)
    public static vec L(vec U,float s,vec V) {return new vec(U.x+s*(V.x-U.x),U.y+s*(V.y-U.y));}                        // L(U,s,V): (1-s)U+sV (Linear interpolation between vectors)
    public static vec R(vec U, float s, vec V) {float a = angle(U,V); vec W = U.makeRotatedBy(s*a);                     // R(U,s,V): interpolation (of angle and length) between U and V
        float u = n(U); float v=n(V); S((u+s*(v-u))/u,W); return W ; }

    // measure points (equality, distance)
    public static boolean isSame(pt A, pt B) {return (A.x==B.x)&&(A.y==B.y) ;}                                         // isSame(A,B): A==B
    public static boolean isSame(pt A, pt B, float e) {return ((abs(A.x-B.x)<e)&&(abs(A.y-B.y)<e));}                   // isSame(A,B,e): ||A-B||<e
    public static float d(pt P, pt Q) {return sqrt(d2(P,Q));  }                                                       // d(A,B): ||AB|| (Distance)
    public static float d2(pt P, pt Q) {return sq(Q.x-P.x)+sq(Q.y-P.y); }                                             // d2(A,B): AB*AB (Distance squared)
    // measure vectors (dot product,  norm, parallel)
    public static float dot(vec U, vec V) {return U.x*V.x+U.y*V.y; }                                                    // dot(U,V): U*V (dot product U*V)
    public static float n(vec V) {return sqrt(dot(V,V));}                                                               // n(V): ||V|| (norm: length of V)
    public static float n2(vec V) {return sq(V.x)+sq(V.y);}                                                             // n2(V): V*V (norm squared)
    public static boolean parallel (vec U, vec V) {return dot(U,R(V))==0; }
    // Scale, rotate, translate points
    public static pt S(float s, pt A) {return new pt(s*A.x,s*A.y); }                                                                      // S(s,A): sA
    public static pt S(float s, pt A, pt B) {return new pt((s+1)*B.x-s*A.x,(s+1)*B.y-s*A.y); }                                            // S(s,A,B): B+sBA=(1+s)B-sA (scaling of A by s wrt fixed point B)
    public static pt R(pt Q, float a) {float dx=Q.x, dy=Q.y, c=cos(a), s=sin(a); return new pt(c*dx+s*dy,-s*dx+c*dy); }                   // R(Q,a): Q rotated by angle a around the origin
    public static pt R(pt Q, float a, pt P) {float dx=Q.x-P.x, dy=Q.y-P.y, c=cos(a), s=sin(a); return P(P.x+c*dx-s*dy, P.y+s*dx+c*dy); }  // R(Q,a,P): Q rotated by angle a around point P
    public static pt T(pt P, vec V) {return P(P.x + V.x, P.y + V.y); }                                                 // T(P,V): P+V (P transalted by vector V)
    public static pt T(pt P, float s, vec V) {return T(P,S(s,V)); }                                                    // T(P,s,V): P+sV (P transalted by sV)
    public static pt T(pt P, float s, pt Q) { return T(P,s,U(V(P,Q))); }                                              // T(P,s,Q): P+sU(PQ) (transalted P by distance s towards Q)
    // transform vectors
    public static vec U(vec V) {float n = n(V); if (n==0) return new vec(0,0); else return new vec(V.x/n,V.y/n);}      // U(V): V/||V|| (Unit vector : normalized version of V)
    public static vec U(pt P, pt Q) {return U(V(P,Q));}                                                                // U(P,Q): PQ/||PQ| (Unit vector : from P towards Q)
    public static vec R(vec V) {return new vec(-V.y,V.x);}                                                             // R(V): V turned right 90 degrees (as seen on screen)
    public static vec R(vec U, float a) {vec W = U.makeRotatedBy(a);  return W ; }                                     // R(V,a): V rotated by a radians

    //************************************************************************
    //**** ANGLES
    //************************************************************************
    public static float angle (vec U, vec V) {return atan2(dot(R(U),V),dot(U,V)); }                                   // angle(U,V): angle <U,V> (between -PI and PI)
    public static float angle(vec V) {return(atan2(V.y,V.x)); }                                                       // angle(V): angle between <1,0> and V (between -PI and PI)
    public static float angle(pt A, pt B, pt C) {return  angle(V(B,A),V(B,C)); }                                       // angle(A,B,C): angle <BA,BC>
    public static float turnAngle(pt A, pt B, pt C) {return  angle(V(A,B),V(B,C)); }                                   // turnAngle(A,B,): angle <AB,BC> (positive when right turn as seen on screen)
    public static int toDeg(float a) {return (int) (a*180/PI);}                                                           // convert radians to degrees
    public static float toRad(float a) {return(a*PI/180);}                                                             // convert degrees to radians
    public static float positive(float a) { if(a<0) return a+TWO_PI; else return a;}

    public static void println(final String text) {System.out.println(text);}
    public static void print(final String text) {System.out.print(text);}
}

/**
// render points
void v(pt P) {vertex(P.x,P.y);}                                                                      // v(P): next point when drawing polygons between beginShape(); and endShape();
void cross(pt P, float r) {line(P.x-r,P.y,P.x+r,P.y); line(P.x,P.y-r,P.x,P.y+r);}                    // cross(P,r): shows P as cross of length r
void cross(pt P) {cross(P,2);}                                                                       // cross(P): shows P as small cross
void show(pt P, float r) {ellipse(P.x, P.y, 2*r, 2*r);}                                              // show(P,r): draws circle of center r around P
void show(pt P) {ellipse(P.x, P.y, 4,4);}                                                            // show(P): draws small circle around point
void show(pt P, pt Q) {line(P.x,P.y,Q.x,Q.y); }                                                      // show(P,Q): draws edge (P,Q)
void arrow(pt P, pt Q) {arrow(P,V(P,Q)); }                                                            // arrow(P,Q): draws arrow from P to Q
void label(pt P, String S) {text(S, P.x-4,P.y+6.5); }                                                   // label(P,S): writes string S next to P on the screen ( for example label(P[i],str(i));)
void label(pt P, vec V, String S) {text(S, P.x-3.5+V.x,P.y+7+V.y); }                                    // label(P,V,S): writes string S at P+V
// render vectors
void show(pt P, vec V) {line(P.x,P.y,P.x+V.x,P.y+V.y); }                                              // show(P,V): show V as line-segment from P
void show(pt A, pt B, pt C) { beginShape(); v(A); v(B); v(C); endShape(CLOSE); }                      // show triangle
void show(pt A, pt B, pt C, pt D) { beginShape(); v(A); v(B); v(C); v(D); endShape(CLOSE); }                      // show triangle
void show(pt P, float s, vec V) {show(P,S(s,V));}                                                     // show(P,s,V): show sV as line-segment from P
void arrow(pt P, vec V) {show(P,V);  float n=n(V); if(n<0.01) return; float s=max(min(0.2,20./n),6./n);                  // arrow(P,V): show V as arrow from P
     pt Q=T(P,V); vec U = S(-s,V); vec W = R(S(.3,U)); beginShape(); v(T(T(Q,U),W)); v(Q); v(T(T(Q,U),-1,W)); endShape(CLOSE);}
void arrow(pt P, float s, vec V) {arrow(P,S(s,V));}                                                   // arrow(P,s,V): show sV as arrow from P
void arrow(pt P, vec V, String S) {arrow(P,V); T(T(P,0.70,V),15,R(U(V))).showLabel(S,V(-5,4));}      // arrow(P,V,S): show V as arrow from P and print string S on its side

// GUI mouse & canvas (left part [height,height] of window, assuming width>=height)
pt Mouse() {return P(mouseX,mouseY);}                                                                 // Mouse(): returns point at current mouse location
pt Pmouse() {return P(pmouseX,pmouseY);}                                                              // Pmouse(): returns point at previous mouse location
vec MouseDrag() {return new vec(mouseX-pmouseX,mouseY-pmouseY);}                                      // MouseDrag(): vector representing recent mouse displacement
pt MouseInWindow() {float x=mouseX, y=mouseY; x=max(x,0); y=max(y,0); x=min(x,height); y=min(y,height);  return P(x,y);} // mouseInWindow(): nearest square canvas point to mouse
pt ScreenCenter() {return P(height/2,height/2);}                                                       // mouseInWindow(): point in center of square canvas
boolean mouseIsInWindow() {return(((mouseX>0)&&(mouseX<height)&&(mouseY>0)&&(mouseY<height)));}       // mouseIsInWindow(): if mouse is in square canvas
void drag(pt P) { P.x+=mouseX-pmouseX; P.y+=mouseY-pmouseY; }                                          // drag(P) adjusts P by mouse drag vector

 */