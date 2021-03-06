


import java.util.ArrayList;
import java.util.LinkedList;
import javax.management.BadAttributeValueExpException;


/**
 *
 * @author DigitalNet
 */
public class TransportaionProplemContainer {
    public final static boolean MAX = false;
    public final static boolean MIN = true;
    
    private final TransportationProblemHelper tph = new TransportationProblemHelper();
    TransportationProblemHelper.BeginingSolution tphbs ;
    private double[][] c;
    private double[][] x;
    private double[] a;
    private double[] b;
    private final boolean ismin;
    private int CBarI;
    private int CBarJ ;
    private double CBarValue;

    /**
     * Set the value of c
     *
     * @param c new value of c
     */
    private void setC(double[][] c) {
        this.c = c;
    }
    
    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    private void setX(double[][] x) {
        this.x = x;
    }        

    /**
     * Set the value of a
     *
     * @param a new value of a
     */
    private void setA(double[] a) {
        this.a = a;
    }  

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
                ismin = true;
                initNoControl(c,a,b);
                break;
            case 'i':
                ismin = true;
                initNoControl(c, a, b);
                break;
            case 'a':
                ismin = false;
                initNoControl(c, a, b);
                break;
            default:
                throw new IllegalArgumentException(control + " is not a control");
        }
    }

    private void initNoControl(double[][] c, double[] a, double[] b){
        this.setC(c); 
        this.setA(a);
        this.setB(b);
        tphbs = tph.new BeginingSolution();
        this.setX(tphbs.NorthWestCorner(c, a, b));
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
                pivote();
            }
            catch(BadAttributeValueExpException e){
                if(!tphbs.tryShift()){                    
                    System.err.println(e.getMessage());
                    System.err.println(e.getStackTrace());
                    System.err.println(e.getLocalizedMessage());
                    System.err.println(e.toString());
                    return false;
                }
                
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
        if(ismin)
            this.findMinCBar();
        else
            this.findMaxCBar();
        return (this.getCBarValue() >= 0 && ismin)||(this.getCBarValue()<=0 && !ismin);
    }

       

    /**
     * Get the value of minCBarI
     *
     * @return the value of minCBarI
     */
    private int getCBarI() {
        return CBarI;
    }

    /**
     * Set the value of minCBarI
     *
     * @param minCBarI new value of minCBarI
     */
    private void setCBarI(int minCBarI) {
        this.CBarI = minCBarI;
    }

    /**
     * Get the value of minCBarJ
     *
     * @return the value of minCBarJ
     */
    private int getCBarJ() {
        return CBarJ ;
    }

    /**
     * Set the value of minCBarJ
     *
     * @param minCBarJ new value of minCBarJ
     */
    private void setCBarJ(int minCBarJ) {
        this.CBarJ  = minCBarJ;
    } 

    /**
     * Get the value of minCBarValue
     *
     * @return the value of minCBarValue
     */
    private double getCBarValue() {
        return CBarValue;
    }

    /**
     * Set the value of minCBarValue
     *
     * @param minCBarValue new value of minCBarValue
     */
    private void setCBarValue(double minCBarValue) {
        this.CBarValue = minCBarValue;
    }

    private void pivote()throws BadAttributeValueExpException {
//        int []ij = tph.new CirculeFinder(x, minCBarI, minCBarJ, minCBarValue).findCircule();
//        double mu = Math.min(x[ij[0]][minCBarJ], x[minCBarI][ij[1]]);
//        if(mu == 0)
//            throw new BadAttributeValueExpException(null);
//        x[minCBarI][minCBarJ] = mu;
//        x[ij[0]][ij[1]] += mu;
//        x[minCBarI][ij[1]] = (x[minCBarI][ij[1]]==mu?Double.NaN:x[minCBarI][ij[1]]-mu); 
//        x[ij[0]][minCBarJ] = (x[ij[0]][minCBarI]==mu?Double.NaN:x[ij[0]][minCBarI]-mu);
        
        LinkedList<int[]> circule = tph.new CirculeFinder(x, CBarI, CBarJ , CBarValue).findCircule();
        double mu = findMu(circule);
        if(mu == 0)
            throw new BadAttributeValueExpException(null);
        x[CBarI][CBarJ ] = mu;
        boolean add = false;
        LinkedList<int[]> zeros = new LinkedList<>();
        for (int[] is : circule) {
            if(x[is[0]][is[1]]==mu && !add){
                zeros.add(is);
            }
            else
                x[is[0]][is[1]]+= (add?1:-1)*mu;
            add=!add;
        }
        if(zeros.size()<2){
            x[zeros.getFirst()[0]][zeros.getFirst()[1]] = Double.NaN;
        }
        else if(ismin)
        {
            int[] max = zeros.getFirst();
            for (int[] is : zeros) {
                if(c[max[0]][max[1]] < c[is[0]][is[1]]){
                    max = is;
                }
            }
            x[max[0]][max[1]] = Double.NaN;
            for(int[] is:zeros){
                if(is != max)
                    x[is[0]][is[1]]=0;
            }
        }
        else
        {
            int[] min = zeros.getFirst();
            for (int[] is : zeros) {
                if(c[min[0]][min[1]] > c[is[0]][is[1]]){
                    min = is;
                }
            }
            x[min[0]][min[1]] = Double.NaN;
            for(int[] is:zeros){
                if(is != min)
                    x[is[0]][is[1]]=0;
            }
        }
    }
    private double findMu(LinkedList<int[]> ll){
        double mu;
        mu = Double.MAX_VALUE;
        boolean minus = true;
        for (int[] is : ll) {
            if(minus)
                mu = Math.min(mu, x[is[0]][is[1]]);
            minus = !minus;
        }             
        return mu;
    }
    private void findMinCBar() {
        TransportationProblemHelper.CBarFinder cbf = new TransportationProblemHelper.CBarFinder(getM(), getN(),c,x);
        CBarValue = Double.POSITIVE_INFINITY;
        CBarI = -1;
        CBarJ  = -1;
        for(int i=0;i<getM();i++){
            for(int j=0;j<getN();j++){
                if(Double.isNaN(x[i][j])){
                    double cBar = c[i][j]-cbf.getU(i)-cbf.getV(j);
                    if(cBar<getCBarValue()){
                        setCBarValue(cBar);
                        setCBarI(i);
                        setCBarJ(j);
                    }                   
                }
            }
        }
    }
     private void findMaxCBar() {
        TransportationProblemHelper.CBarFinder cbf = new TransportationProblemHelper.CBarFinder(getM(), getN(),c,x);
        CBarValue = Double.NEGATIVE_INFINITY;
        CBarI = -1;
        CBarJ = -1;
        for(int i=0;i<getM();i++){
            for(int j=0;j<getN();j++){
                if(Double.isNaN(x[i][j])){
                    double cBar = c[i][j]-cbf.getU(i)-cbf.getV(j);
                    if(cBar>getCBarValue()){
                        setCBarValue(cBar);
                        setCBarI(i);
                        setCBarJ(j);
                    }                   
                }
            }
        }
    }
    

    @Override
    public String toString(){
        String rslt="";
        for(int i=0;i<getM();i++){
            
            for(int j=0;j<getN();j++){
                rslt+=x[i][j]+"\t";
            }
            rslt += a[i]+"\n";
        }
        for(int j=0;j<getN();j++){
                rslt+=b[j]+"\t";
        }
        return rslt;
    }
}
class TransportationProblemHelper{
    public class BeginingSolution{
        double [][] result;
        int numberOfBasicV;
        int numberOfBasicToBe;
        int zeroI;
        int zeroJ;
        public double[][]NorthWestCorner(double[][] c,double []a,double []b){
            result = nanValue(c.length, c[0].length);
            numberOfBasicV = 0 ;
            numberOfBasicToBe = c.length + c[0].length -1;
            int i = 0 ;
            int j = 0 ;
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
            zeroI = -1;
            zeroJ = -1;
            tryShift();
            return result;
        }
        public boolean tryShift(){
            if(numberOfBasicV == numberOfBasicToBe)
                return false;
            int i = zeroI>0?zeroI:0;  
            int j = zeroJ>0?zeroJ:0;
            while (numberOfBasicV < numberOfBasicToBe){
                while (i < result.length){
                    if((i>zeroI && zeroI>=0)||(i>0 && zeroI<0))
                        j=0;
                    while(j < result[0].length){
                        if (Double.isNaN(result[i][j])){
                            if(zeroI>=0)
                                result[zeroI][zeroJ] = Double.NaN;
                            result[i][j]=0 ;
                            zeroI = i;
                            zeroJ = j;
                            return true;
                        }
                        j++;
                    }
                    i++ ;
                }
            }
            return false;
        }
        public double[][]MinimomCoast(double[][] c,double []a,double []b){
            throw new UnsupportedOperationException("yet to be implemented");
        }
        public double[][]voagle(double[][] c,double []a,double []b){
            throw new UnsupportedOperationException("yet to be implemented");
        }
    }
    private static double[] nanValues(int length){
            double []rslt = new double[length];
            for(int i=0;i<rslt.length;i++){
                rslt[i] = Double.NaN;
            }
            return rslt;
        }
    private static double[][] nanValue(int rows,int columns){
        double [][]rslt = new double[rows][];
            for(int i=0;i<rows;i++){
                rslt[i] = nanValues(columns);
            }
            return rslt;
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
        
        
        
        private void solve(){
            v[0]=0;
            findU(0);       
        }
        private void findU(int index){
            for(int i=0;i<u.length;i++){
                if(Double.isNaN(u[i]) && !Double.isNaN(x[i][index])){
                    u[i] = c[i][index] - v[index];
                    findV(i);
                }
            }
        }
        private void findV(int index){
            for(int j=0;j<v.length;j++){
                if(Double.isNaN(v[j]) && !Double.isNaN(x[index][j])){
                    v[j] = c[index][j] - u[index];
                    findU(j);
                }
            }
        }
        
    }
    public class CirculeFinder{
        private final static boolean ROW = true;
        private final static boolean COLUMN = false;
        private final  ArrayList<Integer> row;
        private final  ArrayList<Integer> column;
        private LinkedList<int[]> circule;
        private final double[][] x;
        private final double[][] copyX;
        private final int minCBarI;
        private final int minCBarJ;
        private final double minCBarValue;
        private int [][] rslt;
        private boolean searchrow;
        
        public CirculeFinder(double[][] x, int minCBarI, int minCBarJ, double minCBarValue) {
            this.x = x;
            copyX = copyMatrix(x);
            this.minCBarI = minCBarI;
            this.minCBarJ = minCBarJ;
            this.minCBarValue = minCBarValue;
            row = new ArrayList<>();
            column = new ArrayList<>();
        }
        
        private double[][] copyMatrix(double [][]mat){
            double[][]result = new double[mat.length][];
            for(int i=0;i<mat.length;i++){
                result[i]=new double[mat[i].length];
                System.arraycopy(mat[i], 0, result[i], 0, mat[i].length);
            }
            return result;
        }
        /*
        *return two corners of the cirule that contain minCBar
        *the first row contain i of the corners
        *the second row contain j of the corners
        */
//        public int[] findCircule()throws BadAttributeValueExpException{
//            setRow();
//            setColumn();
//            for (Integer j : row) {
//                for (Integer i : column) {
//                    if(!Double.isNaN(x[i][j]))
//                        return new int[]{i,j};
//                }
//            }
//            throw new BadAttributeValueExpException(null);
////            throw new Exception("no circule was found");
//        }
        private void setRow(){
            for(int j=0;j<x[0].length;j++){
                if(!Double.isNaN(x[minCBarI][j]) && x[minCBarI][j]>=minCBarValue){
                    row.add(j);
                }
            }
        }
        private void setColumn(){
            for(int i=0;i<x.length;i++){
                if(!Double.isNaN(x[i][minCBarJ]) && x[i][minCBarJ]>=minCBarValue){
                    column.add(i);
                }
            }
        }
        public LinkedList<int[]> findCircule(){
            circule = new LinkedList<>();            
            circule.add(new int[]{minCBarI,minCBarJ});
            searchrow = true;
            if(!search()){
                searchrow = false;
                search();
            }
            circule.removeFirst();
            return circule;
        }
        
        private boolean search(){
            if((circule.getLast()[0]==minCBarI || circule.getLast()[1]==minCBarJ) && circule.size() > 3)
                return true;
            LinkedList<int[]> possible;
            possible = searchrow==ROW?searchRow():searchColumn();
            if(possible == null)
                return false;
            for (int[] is : possible) {
                double val = copyX[is[0]][is[1]];
                copyX[is[0]][is[1]] = Double.NaN;
                circule.add(is);
                searchrow = !searchrow;
                if(search()){
                    return true;
                }
                circule.removeLast();
                copyX[is[0]][is[1]] = val;
            }
            return false;
        }
        private LinkedList<int[]> searchRow(){
            LinkedList<int[]> result = new LinkedList<>();
            for(int j=0;j<copyX[0].length;j++){
                if(!Double.isNaN(copyX[circule.getLast()[0]][j])){
                    result.add(new int[]{circule.getLast()[0],j});
                }
            }
            return (result.size()>0?result:null);
        }
        private LinkedList<int[]> searchColumn(){
            LinkedList<int[]> result = new LinkedList<>();
            for(int i=0;i<copyX.length;i++){
                if(!Double.isNaN(copyX[i][circule.getLast()[1]])){
                    result.add(new int[]{i,circule.getLast()[1]});
                }
            }
            return (result.size()>0?result:null);
        }
    }
}