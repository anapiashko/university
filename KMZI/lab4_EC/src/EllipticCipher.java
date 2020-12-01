import java.util.ArrayList;
import java.util.List;

public class EllipticCipher {

    private EllipticCurve ec = new EllipticCurve(-1, 1, 751);
    private EPoint G = new EPoint();
    private EPoint Pb;
    private int[] k;
    private ArrayList<EPoint> Pm = new ArrayList<>();
    private int nb;

    public EllipticCipher(EPoint ep){
        Pb = new EPoint(ec,ep.getX(), ep.getY());
    }

    public void setG(EPoint ep){
        G = new EPoint(ec,ep.getX(), ep.getY());
    }

    public void setNb(int nb){
        this.nb = nb;
    }

    public void setK(int[] k){
        this.k = k;
    }

    public void setP(List<EPoint> Pm){
        this.Pm.addAll(Pm);
    }

    public ArrayList<EPoint> encode(){
        ArrayList<EPoint> cipher = new ArrayList<>();
        EPoint x1, x2;
        for(int i = 0; i < Pm.size(); i++){
            x1 = G.multiply(k[i]);
            x2 = Pm.get(i).add(Pb.multiply(k[i]));
            cipher.add(x1);
            cipher.add(x2);
        }
        return cipher;
    }

    ArrayList<EPoint> decode(List<EPoint> cipher){
        ArrayList<EPoint> plain_text = new ArrayList<>();
        EPoint x;
        for(int i = 0; i < cipher.size(); i +=2){
            x = cipher.get(i+1)
                    .subtract(
                            cipher.get(i).multiply(nb)
                    );
            plain_text.add(x);
        }
        return plain_text;
    }
}
