
package transportaionproplem;


/**
 *
 * @author DigitalNet
 */
public class TransportaionProplemContainer {
    
    private double[][] c;

    /**
     * Set the value of c
     *
     * @param c new value of c
     */
    private void setC(double[][] c) {
        this.c = c;
    }

    
    private double[][] x;

    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    private void setX(double[][] x) {
        this.x = x;
    }

        private double[] a;

    /**
     * Set the value of a
     *
     * @param a new value of a
     */
    private void setA(double[] a) {
        this.a = a;
    }

        private double[] b;

    /**
     * Set the value of b
     *
     * @param b new value of b
     */
    private void setB(double[] b) {
        this.b = b;
    }

    public TransportaionProplemContainer(double[][] c, double[] a, double[] b,char control) {
        switch (control) {
            case '0':
                initNoControl(c,a,b);
                break;
            default:
                throw new IllegalArgumentException(control + " is not a control");
        }
    }

    private void initNoControl(double[][] c, double[] a, double[] b){
        this.setC(c); 
        this.setA(a);
        this.setB(b);
        this.setX(BeginingSolution.NorthWestCorner(c, a, b));
    }
    
    public int getN(){
        return c[0].length;
    }
    public int getM(){
        return c.length;
    }
    
    public boolean findOptimal(){
        if(!this.isFeasable()) return false;
        while(!this.isOptimal()){
            pivotCBar();
        }
        return true;
    }

    private boolean isFeasable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean isOptimal() {
        this.findCBar();
        return this.getMinCBarValue() >= 0;
    }

        private int minCBarI;

    /**
     * Get the value of minCBarI
     *
     * @return the value of minCBarI
     */
    private int getMinCBarI() {
        return minCBarI;
    }

    /**
     * Set the value of minCBarI
     *
     * @param minCBarI new value of minCBarI
     */
    private void setMinCBarI(int minCBarI) {
        this.minCBarI = minCBarI;
    }

        private int minCBarJ;

    /**
     * Get the value of minCBarJ
     *
     * @return the value of minCBarJ
     */
    private int getMinCBarJ() {
        return minCBarJ;
    }

    /**
     * Set the value of minCBarJ
     *
     * @param minCBarJ new value of minCBarJ
     */
    private void setMinCBarJ(int minCBarJ) {
        this.minCBarJ = minCBarJ;
    }

        private double minCBarValue;

    /**
     * Get the value of minCBarValue
     *
     * @return the value of minCBarValue
     */
    private double getMinCBarValue() {
        return minCBarValue;
    }

    /**
     * Set the value of minCBarValue
     *
     * @param minCBarValue new value of minCBarValue
     */
    private void setMinCBarValue(double minCBarValue) {
        this.minCBarValue = minCBarValue;
    }

    private void pivotCBar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void findCBar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
class BeginingSolution{
    public static double[][]NorthWestCorner(double[][] c,double []a,double []b){
        throw new UnsupportedOperationException("yet to be implemented");
    }
    public static double[][]MinimomCoast(double[][] c,double []a,double []b){
        throw new UnsupportedOperationException("yet to be implemented");
    }
    
    public static double[][]voagle(double[][] c,double []a,double []b){
        throw new UnsupportedOperationException("yet to be implemented");
    }
}