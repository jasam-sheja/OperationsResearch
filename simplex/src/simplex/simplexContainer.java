package simplex;

/**
 *
 * @author DigitalNet solving for max z
 */
class simplexContainer {
    /*
     i indecates a row 
     j indecates a coulom
     */

    public final boolean MAX = true;
    public final boolean MIN = false;

    double[][] table;
    int[] slacks;
    int[] variables;
    boolean state = MAX;

    public simplexContainer(double[][] table, char control) {
        switch (control) {
            case '0':
                initNoControl(table);
                break;
            case 'f':
                init(table);
                break;
            default:
                throw new IllegalArgumentException(control + " is not a control");
        }
    }
    
    private void initNoControl(double[][] table) {
        this.table = table;
        variables = new int[table[0].length - 1];
        for (int j = 0; j < table[0].length - 1; j++) {
            variables[j] = j + 1;
        }
        slacks = new int[table.length - 1];
        for (int i = 0; i < table.length - 1; i++) {
            slacks[i] = i + table[0].length;
        }
    }
    
    /*
     *the last coulom contain the control ismax,bigger,smaller and equal
     */
    private void init(double[][] table) {
        state = table[0][table[0].length - 1] > 0;
        int bigger = 0;
        int smaller = 0;
        int equal = 0;
        for (int i = 1; i < table.length; i++) {
            if (table[i][table[0].length - 1] > 0) {
                bigger++;
            } else if (table[i][table[0].length - 1] < 0) {
                smaller++;
            } else {
                equal++;
            }
        }

        variables = new int[table[0].length - 2];
        for (int j = 0; j < getVariablesCount(); j++) {
            variables[j] = j + 1;
        }
        slacks = new int[bigger + smaller + 2 * equal];
        for (int i = 0; i < getSlacksCount(); i++) {
            slacks[i] = i + table[0].length-1;
        }

        this.table = new double[getSlacksCount() + 1][getVariablesCount() + 1];
        
        int factor = state == MAX ? -1 : 1;
        for (int j = 0; j < getVariablesCount() + 1; j++) {
            this.table[0][j] = table[0][j] * factor;
        }
        for (int i = 1; i < table.length; i++) {
            factor = table[i][table[0].length - 1] > 0 ? -1 : 1;
            for (int j = 0; j < getVariablesCount() + 1; j++) {
                this.table[i][j] = table[i][j] * factor;
            }
            if (table[i][table[0].length - 1] == 0) {
                factor = -1;
                equal--;
                for (int j = 0; j < getVariablesCount() + 1; j++) {
                    this.table[getSlacksCount() - equal][j] = table[i][j] * factor;
                }
            }
        }
    }

    private boolean isFeasible() {
        for (int i = 1; i < table.length; i++) {
            if (table[i][table[0].length - 1] < 0) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    private boolean isDualFeasible(){
        throw new UnsupportedOperationException("this function also may be needed but for the time being it is not");
//        for(int j=0;j<table[0].length-1;j++){
//            if(table[0][j]<0)
//                return true;
//        }            
//        return false;
    }
    private boolean isOptimal() {
        for (int j = 0; j < table[0].length - 1; j++) {
            if (table[0][j] < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isBounded() {
        for (int j = 0; j < table[0].length - 1; j++) {
            if (couldBeIn(j)) {
                for (int i = 1; i < table.length; i++) {
                    if (table[i][j] > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isDualBounded() {
        for (int i = 1; i < getSlacksCount() + 1; i++) {
            if (couldBeOut(i)) {
                for (int j = 0; j < getVariablesCount(); j++) {
                    if (table[i][j] < 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /*
     use only with primal fucntions
     */

    private boolean couldBeIn(int j) {
        return (table[0][j] < 0);
    }

    /*
     use only with dual functions
     */
    private boolean couldBeOut(int i) {
        return (table[i][table[i].length - 1] < 0);
    }
    /*
     there could be an optimal value
     */

    private boolean isNumber() {
        throw new UnsupportedOperationException("can be used to tell if there is a sulution or not or if it it infinit");
    }

    /*
     used when the dectionary is feasible (primal)
     usec to find the out variable using @int selectedOutVariable(selectedInVariable)
     */
    private int selecetInVariable() {
        return minMutiplier();
    }
    /*
     used when the dectionary is feasible (primal)
     get selectedInVariable using @int selecetInVariable()
     */

    private int selectOutVariable(int selectedInVariable) {
        double[] miniCoulum = new double[table.length];
//        miniCoulum[0] = Double.MAX_VALUE;
        for (int i = 1; i < table.length; i++) {
            double factor = table[i][selectedInVariable];
            miniCoulum[i] = factor > 0 ? table[i][table[0].length - 1] / factor : Double.MAX_VALUE;
        }
        return mathHelper.IndexMinArray(miniCoulum, 1, miniCoulum.length);
    }

    /*
     used when the dectionary is infeasible (dual)
     usec to find the in variable using @int selectedInVariable(selectedInVariable)
     */
    private int selectOutVariable() {
        return minRightHand();
    }
    /*
     used when the dectionary is infeasible (dual)
     get selectedInVariable using @int selecetOutVariable()
     */

    private int selecetInVariable(int selectedOutVariable) {
        double[] miniRow = new double[getVariablesCount()];
        for (int j = 0; j < miniRow.length; j++) {
            double factor = table[selectedOutVariable][j];
            miniRow[j] = factor < 0 ? table[0][j] / factor : Double.MAX_VALUE;
        }
        return mathHelper.IndexMinArray(miniRow, 0, miniRow.length);
    }

    private void pivote(int out, int in) {
        double factor = table[out][in];
        double[] newVariableCoulum = new double[getSlacksCount() + 1];
        for (int i = 0; i < newVariableCoulum.length; i++) {
            newVariableCoulum[i] = -table[i][in] / factor;
        }
        for (int j = 0; j < table[out].length; j++) {
            table[out][j] /= factor;
        }

        for (int i = 0; i < table.length; i++) {
            if (i == out) {
                table[out][in] /= factor;
            } else {
                double factor2 = table[i][in];
                for (int j = 0; j < table[i].length; j++) {
                    if (j == in) {
                        table[i][in] = newVariableCoulum[i];
                    } else {
                        table[i][j] -= table[out][j] * factor2;
                    }
                }
            }
        }

        int t = slacks[out - 1];
        slacks[out - 1] = variables[in];
        variables[in] = t;
    }

    public int getSlack(int index) {
        return slacks[index];
    }

    public int getVariable(int index) {
        return variables[index];
    }
    /*
     *used to corret the table if and only if the state is MIN
     */

    private void correct() {
        if (state == MAX) {
            return;
        }
        for (int j = 0; j < getVariablesCount() + 1; j++) {
            table[0][j] = -table[0][j];
        }
    }

    /*
    *there is not need for it now because the Pivote funtion can do both
    */
    @Deprecated
    private void dualPivote(int out, int in) {
        double factor = table[out][in];
        double[] newVariableCoulum = new double[getSlacksCount() + 1];
        for (int i = 0; i < newVariableCoulum.length; i++) {
            newVariableCoulum[i] = -table[i][in] / factor;
        }
        for (int j = 0; j < table[out].length; j++) {
            table[out][j] /= factor;
        }

        for (int i = 0; i < table.length; i++) {
            if (i == out) {
                table[out][in] /= factor;
            } else {
                double factor2 = table[i][in];
                for (int j = 0; j < table[i].length; j++) {
                    if (j == in) {
                        table[i][in] = newVariableCoulum[i];
                    } else {
                        table[i][j] -= table[out][j] * factor2;
                    }
                }
            }
        }

        int t = slacks[out - 1];
        slacks[out - 1] = variables[in];
        variables[in] = t;
    }

    public boolean findOptimal() {
        while (true) {
            if (isFeasible()) {
                if (isOptimal()) {
                    correct();
                    return true;
                } else {
                    if (!isBounded()) {
                        return true;
                    }
                    int in = selecetInVariable();
                    int out = selectOutVariable(in);
                    pivote(out, in);
                }
            } else if (isDualBounded()) {
                int out = selectOutVariable();
                int in = selecetInVariable(out);
                pivote(out, in);
            } else {
                return false;
            }
        }
    }

    public int getSlacksCount() {
        return slacks.length;
    }

    public int getVariablesCount() {
        return variables.length;
    }

    @Override
    public String toString() {
        String st = "";
        for (double[] row : table) {
            for (double element : row) {
                st += "\t" + element;
            }
            st += "\n";
        }
        return st;
    }

    private int minMutiplier() {
        return mathHelper.IndexMinArray(table[0], 0, table[0].length - 1);
    }

    private int minRightHand() {
        double[] temp = new double[getSlacksCount() + 1];
        for (int i = 1; i < table.length; i++) {
            temp[i] = table[i][getVariablesCount()];
        }
        return mathHelper.IndexMinArray(temp, 1, temp.length);
    }

}

class mathHelper {

    public static int IndexMinArray(double[] t, int start, int end) {
        if (end > t.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        double minValue = Double.MAX_VALUE;
        int minIndex = -1;
        for (int j = start; j < end; j++) {
            if (t[j] < minValue) {
                minValue = t[j];
                minIndex = j;
            }
        }
        if (minIndex < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return minIndex;
    }

}
