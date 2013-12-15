
/**
 *
 * @author DigitalNet
 */
public class TransportaionProplem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double nan = Double.NaN;
        System.out.println(Double.isNaN(nan));
        double [][]ct;
        double []at;
        double []bt;
//        ct = new double[][]{{2,3,11,7},{1,0,6,1},{5,8,15,9}};
//        at = new double[]{6,1,10};
//        bt = new double[]{7,5,3,2};
        ct = new double[][]{{10,2,20,11},{12,7,9,20},{4,14,16,18}};
        at = new double[]{15,25,10};
        bt = new double[]{5,15,15,15};
        TransportaionProplemContainer tpc = new TransportaionProplemContainer(ct, at, bt, '0');
//        ct = new double[][]{{-2,-3,-11,-7},{-1,-0,-6,-1},{-5,-8,-15,-9}};
//        at = new double[]{6,1,10};
//        bt = new double[]{7,5,3,2};
//        ct = new double[][]{{10,2,20,11},{12,7,9,20},{4,14,16,18}};
//        at = new double[]{15,25,10};
//        bt = new double[]{5,15,15,15};
//        TransportaionProplemContainer tpc = new TransportaionProplemContainer(ct, at, bt, 'a');
        
        tpc.findOptimal();
        System.out.println(tpc.toString());
    }
    
}




