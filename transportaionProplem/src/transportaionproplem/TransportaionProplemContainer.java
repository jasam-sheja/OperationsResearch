
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
        double [][] result;
        result = new double[c.length ][c[0].length];
        int numberOfBasicV = 0 ;
        int i = 0 ;
        int j = 0 ;
        for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
               result[k][l] =Double.NaN ;
            }
        }
        while(i <c.length ){
            while (j<c[0].length){
                if (a[i]<b[j]){
                    result[i][j]=a[i] ; 
                    b[j]-=a[i] ;
                    a[i]=0;
                    i++ ;
                    numberOfBasicV++ ;
                }
                else{
                    result[i][j]=b[j];
                    a[i]-=b[j];
                    b[j]=0;
                    j++ ;
                    numberOfBasicV++ ;
                    if (a[i]==0)
                        i++;
                
                }
            
            }
        
        }
        i = 0 ;  j= 0 ; 
        while (numberOfBasicV<(c.length + c[0].length -1)){
            
            boolean done =false ;
            while ( (!done) && (i <c.length ) ){
                while((!done)&&(j < c[0].length)){
                    if (result[i][j] == Double.NaN){
                        result[i][j]=0 ;
                        numberOfBasicV++ ;
                        done=true ;
                    }
                    j++;
                }
                i++ ;
            }
        }
            
       
        return result ;
    }
    public static double[][]MinimomCoast(double[][] c,double []a,double []b){
        throw new UnsupportedOperationException("yet to be implemented");
    }
    
    public static double[][]voagle(double[][] c,double []a,double []b){
        throw new UnsupportedOperationException("yet to be implemented");
    }
}