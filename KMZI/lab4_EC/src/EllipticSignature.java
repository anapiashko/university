public class EllipticSignature {
    private EllipticCurve ec = new EllipticCurve(-1, 1, 751);
    EPoint G, Q;
    int n;
    int e;
    int d;
    int k;
    public EllipticSignature(EPoint ep , int n, int e, int d, int k){
        G = new EPoint(ec,ep.getX(), ep.getY());
        this.n = n;
        this.e = e;
        this.d = d;
        this.k = k;
    }

    public void setG(EPoint ep){
        G = new EPoint(ec, ep.getX(), ep.getY());
    }

    public int[] sign(){
        EPoint kG = G.multiply(k);
        int r = kG.getX() % n;
        int z = mod_inverse(k, n);
        int s = (z * (e + d * r)) % n;
        return new int[]{r,s};
    }

    public boolean check(int[] rs, int e, EPoint Q){
        if (rs[0] >= 1 && rs[1] >= 1 && rs[0] <= n - 1 && rs[1] <= n - 1) {
            int v = mod_inverse(rs[1], n);
            int u1 = e * v % n;
            int u2 = rs[0] * v % n;
            EPoint x = G.multiply(u1).add(Q.multiply(u2));
            return rs[0] == (x.getX() % n);
        } else {
            return false;
        }
    }

    int power(int x, int y, int m){
        if(y == 0) return 1;
        int p = power(x, y/2, m) % m;
        p = (p*p) % m;
        return (y%2==0)?p:(x*p)%m;
    }
    int mod_inverse(int a, int m){
        return power(a, m-2, m);
    }
}
