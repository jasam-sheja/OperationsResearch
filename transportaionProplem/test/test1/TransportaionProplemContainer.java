
package test1;

import java.util.ArrayList;


/**
 *
 * @author DigitalNet
 */
public class TransportaionProplemContainer {
    private final TransportationProblemHelper tph = new TransportationProblemHelper();
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
        this.setX(tph.new BeginingSolution().NorthWestCorner(c, a, b));
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
            try{
                pivotCBar();
            }
            catch(Exception e){
                System.err.println(e.getMessage());
                System.err.println(e.getStackTrace());
                return false;
            }
        }
        return true;
    }

    private boolean isFeasable() {
        int sumA = 0;
        for(int i=0;i<getM();i++){
            sumA += a[i];
        }
        int sumB = 0;
        for(int j=0;j<getN();j++){
            sumB += b[j];
        }
        return sumA == sumB;
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

    private void pivotCBar()throws Exception {
        int []ij = tph.new CirculeFinder(x, minCBarI, minCBarJ, minCBarValue).findCircule();
        double mu = Math.min(x[ij[0]][minCBarJ], x[minCBarI][ij[1]]);
        x[minCBarI][minCBarJ] = minCBarValue+mu;
        x[ij[0]][ij[1]] += mu;
        x[minCBarI][ij[1]] = (x[minCBarI][ij[1]]==mu?Double.NaN:x[minCBarI][ij[1]]-mu); 
        x[ij[0]][minCBarI] = (x[ij[0]][minCBarI]==mu?Double.NaN:x[ij[0]][minCBarI]-mu);
    }

    private void findCBar() {
        TransportationProblemHelper.CBarFinder cbf = new TransportationProblemHelper.CBarFinder(getM(), getN(),c,x);
        minCBarValue = Double.NaN;
        minCBarI = -1;
        minCBarJ = -1;
        for(int i=0;i<getM();i++){
            for(int j=0;j<getN();j++){
                if(x[i][j] == Double.NaN){
                    double cBar = c[i][j]-cbf.getU(i)-cbf.getV(j);
                    if(cBar<getMinCBarValue()){
                        setMinCBarValue(cBar);
                        setMinCBarI(i);
                        setMinCBarJ(j);
                    }                   
                }
            }
        }
    }
    
}
class TransportationProblemHelper{
    public class BeginingSolution{
        public double[][]NorthWestCorner(double[][] c,double []a,double []b){
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
        public double[][]MinimomCoast(double[][] c,double []a,double []b){
            throw new UnsupportedOperationException("yet to be implemented");
        }
        public double[][]voagle(double[][] c,double []a,double []b){
            throw new UnsupportedOperationException("yet to be implemented");
        }
    }
    public static class CBarFinder{
        private final double []u ;
        private final double []v ;

        private final double[][]x;
        private final double[][]c;
        
        public double[] getU(){
            return u;
        }
        public double[] getV(){
            return v;
        }
        public double getU(int index){
            return u[index];
        }
        public double getV(int index){
            return v[index];
        }
        public CBarFinder(int un, int vn,double[][] c,double[][] x) {
            this.u = nanValues(un);
            this.v = nanValues(vn);
            this.x = x;
            this.c = c;
            solve();
        }
        
        private double[] nanValues(int length){
            double []rslt = new double[length];
            for(int i=0;i<rslt.length;i++){
                rslt[i] = Double.NaN;
            }
            return rslt;
        }
        
        private void solve(){
            v[0]=0;
            findU(0);       
        }
        private void findU(int index){
            for(int i=0;i<u.length;i++){
                if(u[i]==Double.NaN && x[i][index]!=Double.NaN){
                    u[i] = c[i][index] - v[index];
                    findV(i);
                }
            }
        }
        private void findV(int index){
            for(int j=0;j<v.length;j++){
                if(v[j]==Double.NaN && x[index][j]!=Double.NaN){
                    v[j] = c[index][j] - u[index];
                    findU(j);
                }
            }
        }
        
    }
    public class CirculeFinder{
        private  ArrayList<Integer> row;
        private  ArrayList<Integer> column;
        private final double[][] x;
        private final int minCBarI;
        private final int minCBarJ;
        private final double minCBarValue;

        public CirculeFinder(double[][] x, int minCBarI, int minCBarJ, double minCBarValue) {
            this.x = x;
            this.minCBarI = minCBarI;
            this.minCBarJ = minCBarJ;
            this.minCBarValue = minCBarValue;
        }
        
        /*
        *return two corners of the cirule that contain minCBar
        *the first row contain i of the corners
        *the second row contain j of the corners
        */
        public int[] findCircule()throws Exception{
            findRow();
            findColumn();
            for (Integer j : row) {
                for (Integer i : column) {
                    if(x[i][j]!=Double.NaN)
                        return new int[]{i,j};
                }
            }
            throw new Exception("no circule was found");
        }
        private void findRow(){
            for(int j=0;j<x[0].length;j++){
                if(x[minCBarI][j]!=Double.NaN && x[minCBarI][j]>=minCBarValue){
                    row.add(j);
                }
            }
        }
        private void findColumn(){
            for(int i=0;i<x.length;i++){
                if(x[i][minCBarI]!=Double.NaN && x[i][minCBarI]>=minCBarValue){
                    column.add(i);
                }
            }
        }
        
    }
}