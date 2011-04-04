package pcanvas.legacy;

import static pcanvas.legacy.Functions.L;
import static pcanvas.legacy.Functions.T;
import static pcanvas.legacy.Functions.V;
import static pcanvas.legacy.Functions.angle;
import static pcanvas.legacy.Functions.d;
import static pcanvas.legacy.Functions.dot;
import static pcanvas.legacy.Functions.print;
import static processing.core.PApplet.abs;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.max;
import static processing.core.PApplet.min;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.sq;
import static processing.core.PApplet.sqrt;
import static processing.core.PApplet.tan;

/**
 * Legacy pt, point object, courtesy of Jarek Rossignac.
 */
public class pt {
    public float x=0,y=0,z=0;
    // CREATE
    public pt () {}
    public pt (float px, float py) {x = px; y = py;}
    public pt (float px, float py, float pz) {x = px; y = py; z = pz;}
    public pt (pt P) {x = P.x; y = P.y;}
    public pt (pt P, vec V) {x = P.x+V.x; y = P.y+V.y;}
    public pt (pt P, float s, vec V) {x = P.x+s*V.x; y = P.y+s*V.y;}
    public pt (pt A, float s, pt B) {x = A.x+s*(B.x-A.x); y = A.y+s*(B.y-A.y);}

    // MODIFY
    public void reset() {x = 0; y = 0;}                                       // P.reset(): P=(0,0)
    public void set(float px, float py) {x = px; y = py;}                     // P.set(x,y): P=(x,y)
    public void set(pt Q) {x = Q.x; y = Q.y;}                                 // P.set(Q): P=Q (copy)
    public void add(float u, float v) {x += u; y += v;}                       // P.add(u,v): P+=<u,v>
    public void add(vec V) {x += V.x; y += V.y;}                              // P.add(V): P+=V
    public void add(float s, vec V) {x += s*V.x; y += s*V.y;}                 // P.add(s,V): P+=sV
    public void add(pt Q) {x += Q.x; y += Q.y;}                               // P.add(Q): P+=Q
    public void scale(float s) {x*=s; y*=s;}                                  // P.scale(s): P*=s
    public void scale(float s, pt C) {x*=C.x+s*(x-C.x); y*=C.y+s*(y-C.y);}    // P.scale(s,C): P=L(C,s,P);
    public void rotate(float a) {float dx=x, dy=y, c=cos(a), s=sin(a); x=c*dx+s*dy; y=-s*dx+c*dy; }     // P.rotate(a): rotate P around origin by angle a in radians
    public void rotate(float a, pt P) {float dx=x-P.x, dy=y-P.y, c=cos(a), s=sin(a); x=P.x+c*dx+s*dy; y=P.y-s*dx+c*dy; }   // P.rotate(a,G): rotate P around G by angle a in radians

    public void setTo(float px, float py) {x = px; y = py;}
    public void setTo(pt P) {x = P.x; y = P.y;}
    //void setToMouse() { x = mouseX; y = mouseY; }
    //void moveWithMouse() { x += mouseX-pmouseX; y += mouseY-pmouseY; }
    public void addVec(vec V) {x += V.x; y += V.y;}
    public void translateBy(vec V) {x += V.x; y += V.y;}
    public void translateBy(float u, float v) {x += u; y += v;}
    public void translateBy(float s, vec V) {x += s*V.x; y += s*V.y;}
    public void translateToTrack(float s, pt P) {setTo(T(P,s,V(P,this)));}       // translate by distance s towards P
    public void translateTowards(float s, pt P) {x+=s*(P.x-x);  y+=s*(P.y-y); }  // transalte by ratio s towards P
    public void translateTowardsBy(float s, pt P) {vec V = this.makeVecTo(P); V.normalize(); this.translateBy(s,V); }
    public void track(float s, pt P) {setTo(T(P,s,V(P,this)));}
    public void scaleBy(float f) {x*=f; y*=f;}
    public void scaleBy(float u, float v) {x*=u; y*=v;}
    public void addPt(pt P) {x += P.x; y += P.y;}        // incorrect notation, but useful for computing weighted averages
    public void addScaled(float s, vec V) {x += s*V.x; y += s*V.y;}
    public void addScaled(float s, pt P)   {x += s*P.x; y += s*P.y;}
    public void addScaledPt(float s, pt P) {x += s*P.x; y += s*P.y;}        // incorrect notation, but useful for computing weighted averages
    public void rotateBy(float a) {float dx=x, dy=y, c=cos(a), s=sin(a); x=c*dx+s*dy; y=-s*dx+c*dy; }     // around origin
    public void rotateBy(float a, pt P) {float dx=x-P.x, dy=y-P.y, c=cos(a), s=sin(a); x=P.x+c*dx+s*dy; y=P.y-s*dx+c*dy; }   // around point P
    public void rotateBy(float s, float t, pt P) {float dx=x-P.x, dy=y-P.y; dx-=dy*t; dy+=dx*s; dx-=dy*t; x=P.x+dx; y=P.y+dy; }   // s=sin(a); t=tan(a/2);
    public void clipToWindow(float height) {x=max(x,0); y=max(y,0); x=min(x,height); y=min(y,height); }

    // OUTPUT POINT
    public pt clone() {return new pt(x,y); }
    public pt make() {return new pt(x,y); }
    public pt makeClone() {return new pt(x,y); }
    public pt makeTranslatedBy(vec V) {return(new pt(x + V.x, y + V.y));}
    public pt makeTranslatedBy(float s, vec V) {return(new pt(x + s*V.x, y + s*V.y));}
    public pt makeTransaltedTowards(float s, pt P) {return(new pt(x + s*(P.x-x), y + s*(P.y-y)));}
    public pt makeTranslatedBy(float u, float v) {return(new pt(x + u, y + v));}
    public pt makeRotatedBy(float a, pt P) {float dx=x-P.x, dy=y-P.y, c=cos(a), s=sin(a); return(new pt(P.x+c*dx+s*dy, P.y-s*dx+c*dy)); }
    public pt makeRotatedBy(float a) {float dx=x, dy=y, c=cos(a), s=sin(a); return(new pt(c*dx+s*dy, -s*dx+c*dy)); }
    public pt makeProjectionOnLine(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return(P.makeTransaltedTowards(a/b,Q)); }
    public pt makeOffset(pt P, pt Q, float r) {
        float a = angle(vecTo(P),vecTo(Q))/2;
        float h = r/tan(a);
        vec T = vecTo(P); T.normalize(); vec N = T.left();
        pt R = new pt(x,y); R.translateBy(h,T); R.translateBy(r,N);
        return R; }

    // OUTPUT VEC
    public vec vecTo(pt P) {return(new vec(P.x-x,P.y-y)); }
    public vec makeVecTo(pt P) {return(new vec(P.x-x,P.y-y)); }
    //vec makeVecToCenter () {return(new vec(x-height/2F,y-height/2F)); }
    public vec makeVecToAverage (pt P, pt Q) {return(new vec((P.x+Q.x)/2.0F-x,(P.y+Q.y)/2.0F-y)); }
    public vec makeVecToAverage (pt P, pt Q, pt R) {return(new vec((P.x+Q.x+R.x)/3.0F-x,(P.y+Q.y+R.x)/3.0F-y)); }
    //vec makeVecToMouse () {return(new vec(mouseX-x,mouseY-y)); }
    public vec makeVecToBisectProjection (pt P, pt Q) {float a=this.disTo(P), b=this.disTo(Q);  return(this.makeVecTo(L(P,a/(a+b),Q))); }
    public vec makeVecToNormalProjection (pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return(this.makeVecTo(L(P,a/b,Q))); }
    public vec makeVecTowards(pt P, float d) {vec V = makeVecTo(P); float n = V.norm(); V.normalize(); V.scaleBy(d-n); return V; }

    // OUTPUT TEST OR MEASURE
    public float disTo(pt P) {return(sqrt(sq(P.x-x)+sq(P.y-y))); }
    //float disToMouse() {return(sqrt(sq(x-mouseX)+sq(y-mouseY))); }
    //boolean isInWindow() {return(((x>0)&&(x<height)&&(y>0)&&(y<height)));}
    public boolean projectsBetween(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return((0<a)&&(a<b)); }
    public float ratioOfProjectionBetween(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return(a/b); }
    public float disToLine(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q).makeUnit().makeTurnedLeft()); return(abs(a)); }
    public boolean isLeftOf(pt P, pt Q) {boolean l=dot(P.makeVecTo(this),P.makeVecTo(Q).makeTurnedLeft())>0; return(l);  }
    public boolean isLeftOf(pt P, pt Q, float e) {boolean l=dot(P.makeVecTo(this),P.makeVecTo(Q).makeTurnedLeft())>e; return(l);  }  boolean isInTriangle(pt A, pt B, pt C) { boolean a = this.isLeftOf(B,C); boolean b = this.isLeftOf(C,A); boolean c = this.isLeftOf(A,B); return((a&&b&&c)||(!a&&!b&&!c));}
    public boolean isInCircle(pt C, float r) {return d(this,C)<r; }  // returns true if point is in circle C,r

    // DRAW , PRINT
    //void show() {ellipse(x, y, height/200, height/200); } // shows point as small dot
    //void show(float r) {ellipse(x, y, 2*r, 2*r); } // shows point as disk of radius r
    //void showCross(float r) {line(x-r,y,x+r,y); line(x,y-r,x,y+r);}
    //void v() {vertex(x,y);}  // used for drawing polygons between beginShape(); and endShape();
    public void write() {print("("+x+","+y+")");}  // writes point coordinates in text window
    //void showLabel(String s, vec D) {text(s, x+D.x-5,y+D.y+4);  }  // show string displaced by vector D from point
    //void showLabel(String s) {text(s, x+5,y+4);  }
    //void showLabel(int i) {text(str(i), x+5,y+4);  }  // shows integer number next to point
    //void showLabel(String s, float u, float v) {text(s, x+u, y+v);  }
    //void showSegmentTo (pt P) {line(x,y,P.x,P.y); } // draws edge to another point
    //void to (pt P) {line(x,y,P.x,P.y); } // draws edge to another point
}
