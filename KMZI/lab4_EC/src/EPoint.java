public class EPoint {

    private EllipticCurve ec;
    private int x;
    private int y;

    public EPoint() {
        x = 0;
        y = 0;
    }

    public EPoint(EllipticCurve ec, int x, int y) {
        if (ec.containPoint(x, y)) {
            this.ec = ec;
            this.x = x;
            this.y = y;
        } else {
            System.out.println("Point does not belong to curve");
            this.x = 0;
            this.y = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public EPoint add(EPoint ep) {
        EPoint new_ep = new EPoint();
        int n = ep.ec.getP();
        if (this.x != ep.x) {
            int lamda_up = ep.y - this.y;
            int lamda_down = ep.x - this.x;
            int lamda = (mod_inverse(lamda_down, n) * (lamda_up % n)) % n;
            if (lamda < 0) lamda += n;

            new_ep.x = (lamda * lamda - this.x - ep.x) % ep.ec.getP();
            if (new_ep.x < 0) new_ep.x += n;
            new_ep.y = (lamda * (this.x - new_ep.x) - this.y) % ep.ec.getP();
            if (new_ep.y < 0) new_ep.y += n;
            return new EPoint(ep.ec, new_ep.x, new_ep.y);
        } else {
            int lamda_up = 3 * this.x * this.x + ep.ec.getA();
            int lamda_down = 2 * this.y;
            int lamda = (mod_inverse(lamda_down, n) * (lamda_up % n)) % n;
            if (lamda < 0) lamda += n;
            new_ep.x = (lamda * lamda - 2 * this.x) % ep.ec.getP();
            if (new_ep.x < 0) new_ep.x += n;
            new_ep.y = (lamda * (this.x - new_ep.x) - this.y) % ep.ec.getP();
            if (new_ep.y < 0) new_ep.y += n;
            return new EPoint(ep.ec, new_ep.x, new_ep.y);
        }
    }

    public EPoint multiply(int n) {
        EPoint res, addend;
        res = this;
        addend = new EPoint(ec,x,y);

        for (int i = 0; i < n-1; i++) {
            res = res.add(addend);
        }
        return new EPoint(this.ec, res.x, res.y);

//        EPoint res, addend;
//        res = this;
//        addend = this;
//        ArrayList<Integer> bits = new ArrayList<>();
//        n--;
//        while (n != 0) {
//            bits.add(n & 1);
//            n >>= 1;
//        }
//        for (int i = 0; i < bits.size(); i++) {
//            if (bits.get(i) == 1) res = res.add(addend);
//            addend = addend.add(addend);
//        }
//        return new EPoint(this.ec, res.x, res.y);
    }

    public EPoint subtract(EPoint ep) {
        EPoint ep_inverse = new EPoint(ep.ec, ep.x, -ep.y);
        return this.add(ep_inverse);
    }

    int power(int x, int y, int m) {
        if (y == 0) return 1;
        int p = power(x, y / 2, m) % m;
        p = (p * p) % m;
        return (y % 2 == 0) ? p : (x * p) % m;
    }

    int mod_inverse(int a, int m) {
        return power(a, m - 2, m);
    }

    @Override
    public String toString() {
        return "{ x=" + x +
                ", y=" + y +
                " }";
    }

}
