public class EllipticCurve {

    private int a;
    private int b;
    private int p;

    public EllipticCurve() {
        a = 0;
        b = 0;
        p = 0;
    }

    public EllipticCurve(int a, int b, int p){
        this.a = a;
        this.b = b;
        this.p = p;
    }

    public boolean containPoint(int x, int y){
        return (y * y) % p == (x * x * x + a * x + b) % p;
    }

    int getP() { return p; }

    int getA() { return a; }

    int getB() {return b; }
}
