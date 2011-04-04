package pcanvas.legacy;

import static pcanvas.legacy.Functions.dot;
import static pcanvas.legacy.Functions.println;
import static processing.core.PApplet.abs;
import static processing.core.PApplet.atan2;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.sq;
import static processing.core.PApplet.sqrt;
/**
 * Legacy vec, vector object, courtesy of Jarek Rossignac.
 */
public class vec {
    public float x=0,y=0,z=0;
    public vec () {}
    public vec (vec V) {x = V.x; y = V.y; z = V.y;}
    public vec (float s, vec V) {x = s*V.x; y = s*V.y; z = s*V.z;}
    public vec (float px, float py) {x = px; y = py;}
    public vec (float px, float py, float pz) {x = px; y = py; z = pz;}
    public vec (pt P, pt Q) {x = Q.x-P.x; y = Q.y-P.y; z = Q.z - P.z;}

    // MODIFY
    public void setTo(float px, float py) {x = px; y = py;}
    public void setTo(pt P, pt Q) {x = Q.x-P.x; y = Q.y-P.y;}
    public void setTo(vec V) {x = V.x; y = V.y;}
    public void scaleBy(float f) {x*=f; y*=f;}
    public void back() {x=-x; y=-y;}
    public void mul(float f) {x*=f; y*=f;}
    public void div(float f) {x/=f; y/=f;}
    public void scaleBy(float u, float v) {x*=u; y*=v;}
    public void normalize() {float n=sqrt(sq(x)+sq(y)); if (n>0.000001) {x/=n; y/=n;}}
    public void add(vec V) {x += V.x; y += V.y;}
    public void add(float s, vec V) {x += s*V.x; y += s*V.y;}
    public void addScaled(float s, vec V) {x += s*V.x; y += s*V.y;}
    public void add(float u, float v) {x += u; y += v;}
    public void turnLeft() {float w=x; x=-y; y=w;}
    public void rotateBy (float a) {float xx=x, yy=y; x=xx*cos(a)-yy*sin(a); y=xx*sin(a)+yy*cos(a); }
    public void clip (float m) {float n=norm(); if(n>m) scaleBy(m/n);}


    // OUTPUT VEC
    public vec make() {return(new vec(x,y));}
    public vec clone() {return(new vec(x,y));}
    public vec makeClone() {return(new vec(x,y));}
    public vec makeUnit() {float n=sqrt(sq(x)+sq(y)); if (n<0.000001) n=1; return(new vec(x/n,y/n));}
    public vec unit() {float n=sqrt(sq(x)+sq(y)); if (n<0.000001) n=1; return(new vec(x/n,y/n));}
    public vec makeScaledBy(float s) {return(new vec(x*s, y*s));}
    public vec makeTurnedLeft() {return(new vec(-y,x));}
    public vec left() {return(new vec(-y,x));}
    public vec makeOffsetVec(vec V) {return(new vec(x + V.x, y + V.y));}
    public vec makeOffsetVec(float s, vec V) {return(new vec(x + s*V.x, y + s*V.y));}
    public vec makeOffsetVec(float u, float v) {return(new vec(x + u, y + v));}
    public vec makeRotatedBy(float a) {float c=cos(a), s=sin(a); return(new vec(x*c-y*s,x*s+y*c)); }
    public vec makeReflectedVec(vec N) { return makeOffsetVec(-2F*dot(this,N),N);}

    // OUTPUT TEST MEASURE
    public float norm() {return(sqrt(sq(x)+sq(y)));}
    public boolean isNull() {return((abs(x)+abs(y)<0.000001));}
    public float angle() {return(atan2(y,x)); }

    // DRAW, PRINT
    public void write() {println("("+x+","+y+")");}
    //void show (pt P) {line(P.x,P.y,P.x+x,P.y+y); }
    //void showAt (pt P) {line(P.x,P.y,P.x+x,P.y+y); }
    /*void showArrowAt (pt P) {
        line(P.x,P.y,P.x+x,P.y+y);
        float n=min(this.norm()/10.,height/50.);
        pt Q=P.makeTranslatedBy(this);
        vec U = this.makeUnit().makeScaledBy(-n);
        vec W = U.makeTurnedLeft().makeScaledBy(0.3F);
        beginShape(); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); Q.v();
                    W.scaleBy(-1); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); endShape(CLOSE); } */

    //void showLabel(String s, pt P) {pt Q = P.makeTranslatedBy(0.5F,this);vec N = makeUnit().makeTurnedLeft(); Q.makeTranslatedBy(3,N).showLabel(s); }

}
