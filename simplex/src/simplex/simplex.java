/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simplex;

/**
 *
 * @author DigitalNet
 */
public class Simplex {

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
//        t = new double[][]{{1,1,0},{-1,-2,-10},{2,1,20}};
//        simplexContainer sc = new simplexContainer(t,'0');
        t = new double[][]{{-3,1,1,0,-1},{1,-2,1,11,-1},{-4,1,2,3,1},{-2,0,1,1,0}};
        simplexContainer sc = new simplexContainer(t,'f');
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
