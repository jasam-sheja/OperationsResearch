/*
للتعامل مع simplexContainer يجب تحويل المسألة إلى max z و المتراجحات إلى أصغر أو يساوي حتى لو كانت القيم اليمينية أصغر من الصفر ثم إدخال الz و المتراجات بالترتيب دون المتغيرات الإضافية
بالطبع هذا أدنى مستوى من طرق الإدخال

الخرج هو الجدول النهائي دون المتغيرات الإضافية
ترتيب المتغيرات يدل عليه slack variable وشكراً
 */

package simplex;

/**
 *
 * @author DigitalNet
 */
public class simplex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double[][] t;
//        t = new double[][]{{-5,-4,0},{6,4,24},{1,2,6},{-1,1,1},{0,1,2}};
//        t = new double[][]{{-2,-1,0},{2,3,8},{1,1,3}};
//        t = new double[][]{{3,2,1,0},{-3,-1,-1,-3},{3,-3,-1,-6},{1,1,1,3}};
//        t = new double[][]{{-1,1,0},{-1,-2,-10},{2,1,20}};
//        t = new double[][]{{-1,-1,0},{-1,-2,-10},{2,1,20}};
        t = new double[][]{{1,1,0},{-1,-2,-10},{2,1,20}};
        simplexContainer sc = new simplexContainer(t);
        System.out.println(sc);
        String st = "\nslacks = \n";
        for(int i=0;i<sc.getSlacksCount();i++){
            st+= "\t"+sc.getSlack(i);
        }
        st+="\nvariables =\n";
        for(int i=0;i<sc.getVariablesCount();i++)
        {
            st+="\t"+sc.getVariable(i);
        }
        System.out.println(st);
        
        System.out.println(sc.findOptimal());
        
        System.out.println("");
        System.out.println(sc);
        st = "\nslacks = \n";
        for(int i=0;i<sc.getSlacksCount();i++){
            st+= "\t"+sc.getSlack(i);
        }
        st+="\nvariables =\n";
        for(int i=0;i<sc.getVariablesCount();i++)
        {
            st+="\t"+sc.getVariable(i);
        }
        System.out.println(st);
    }
    
}
