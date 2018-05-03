
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * Generic Hidden Markov Model.
 * <p>
 * Basic Hidden Markov Model class without learning facilities.
 * </p>
 * @author bctnry
 * (c) Sebastian Lin. 20180427
 * @param <VisibleTokenType> The type of visible tokens. Should be accompanied
 * with an Enumerable Instance.
 * @param <HiddenTokenType> The type of hidden tokens. Should be accompanied
 * with an Enumerable instance.
 */


public class HMM<
        // hidden & visible tokens.
        VisibleTokenType,
        HiddenTokenType
> {
    
    private static <T> int[] smooth(
            Enumerable<T> tInstance,
            Map<T, Integer> matrix
    ) {
        int[] res = new int[matrix.size()];
        matrix.forEach((t, count) -> {
            res[tInstance.toInt(t)] = count == 0? 1 : count;
        });
        return res;
    }
    private static int[] smooth(int[] matrix) {
        int[] res = new int[matrix.length];
        for(int i = 0 ; i < matrix.length ; i++) {
            res[i] = matrix[i] == 0? 1 : matrix[i];
        }
        return res;
    }
    private static <T1, T2> int[][] smooth2D(
            Enumerable<T1> t1Instance,
            Enumerable<T2> t2Instance,
            Map<T1, Map<T2, Integer>> matrix
    ) {
        int[][] res = new int[matrix.size()][matrix.get(t1Instance.fromInt(0)).size()];
        matrix.forEach((t1, map) -> {
            map.forEach((t2, val) -> {
                int cellValue = matrix.get(t1).get(t2);
                res[t1Instance.toInt(t1)][t2Instance.toInt(t2)]
                        = cellValue == 0? 1 : cellValue;
            });
        });
        return res;
    }
    public static int[][] smooth2D(int[][] matrix) {
        int[][] res = new int[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i ++) {
            for(int j = 0; j < matrix[i].length; j ++) {
                res[i][j] = matrix[i][j] == 0? 1 : matrix[i][j];
            }
        }
        return res;
    }
    
    // proofs that proves VisibleTokenType & HiddenTokenType are enums.
    private Enumerable<VisibleTokenType> visibleTokenTypeEnumInstance;
    private Enumerable<HiddenTokenType> hiddenTokenTypeEnumInstance;
    
    /**
     * The transition matrix.
     * In the transition matrix A, A[i][j] should be the probability of
     * the next token being j while the current token is i.
     */
    private double[][] transitionMatrix;
    /**
     * The projection matrix.
     */
    private double[][] projectionMatrix;
    /**
     * The initialization matrix.
     */
    private double[] initializationMatrix;
    
    // constructors.

    /**
     * Default constructor.
     * <p>
     * HMM created using this constructor must be loaded with a projection
     * matrix & a transition matrix before using, or an HMMNoMatrixException
     * will be raised.
     * </p>
     * @param visibleTokenTypeEnumInstance
     * @param hiddenTokenTypeEnumInstance
     */
    public HMM(
            Enumerable<VisibleTokenType> visibleTokenTypeEnumInstance,
            Enumerable<HiddenTokenType> hiddenTokenTypeEnumInstance
    ) {
        this.visibleTokenTypeEnumInstance = visibleTokenTypeEnumInstance;
        this.hiddenTokenTypeEnumInstance = hiddenTokenTypeEnumInstance;
    }

    /**
     * Constructor with arguments.
     * @param visibleTokenTypeEnumInstance The Enumerable instance for VisibleTokenType.
     * @param hiddenTokenTypeEnumInstance The Enumerable instance for HiddenTokenType.
     * @param transitionMatrix The transition matrix.
     * @param projectionMatrix The projection matrix which projects hidden
     * tokens to visible tokens.
     */
    public HMM(
            Enumerable<VisibleTokenType> visibleTokenTypeEnumInstance,
            Enumerable<HiddenTokenType> hiddenTokenTypeEnumInstance,
            double[][] transitionMatrix,
            double[][] projectionMatrix
    ) {
        this.visibleTokenTypeEnumInstance = visibleTokenTypeEnumInstance;
        this.hiddenTokenTypeEnumInstance = hiddenTokenTypeEnumInstance;
        this.transitionMatrix = transitionMatrix;
        this.projectionMatrix = projectionMatrix;
    }
    
    /**
     * Constructor with arguments, this time maps.
     * @param visibleTokenTypeEnumInstance
     * @param hiddenTokenTypeEnumInstance
     * @param initializationMatrix
     * @param transitionMatrix
     * @param projectionMatrix 
     */
    public HMM(
            Enumerable<VisibleTokenType> visibleTokenTypeEnumInstance,
            Enumerable<HiddenTokenType> hiddenTokenTypeEnumInstance,
            Map<HiddenTokenType, Double> initializationMatrix,
            Map<HiddenTokenType, Map<HiddenTokenType, Double>> transitionMatrix,
            Map<HiddenTokenType, Map<VisibleTokenType, Double>> projectionMatrix
    ) {
        this.hiddenTokenTypeEnumInstance = hiddenTokenTypeEnumInstance;
        this.visibleTokenTypeEnumInstance = visibleTokenTypeEnumInstance;
        this.initializationMatrix = new double[hiddenTokenTypeEnumInstance.size()];
        this.transitionMatrix = new double[hiddenTokenTypeEnumInstance.size()][hiddenTokenTypeEnumInstance.size()];
        this.projectionMatrix = new double[hiddenTokenTypeEnumInstance.size()][visibleTokenTypeEnumInstance.size()];
        initializationMatrix.forEach((hiddenToken, probability) -> {
            this.initializationMatrix[hiddenTokenTypeEnumInstance.toInt(hiddenToken)] = probability;
        });
        transitionMatrix.forEach((hiddenToken1, map) -> {
            map.forEach((hiddenToken2, probability) -> {
                this.transitionMatrix
                        [hiddenTokenTypeEnumInstance.toInt(hiddenToken1)]
                        [hiddenTokenTypeEnumInstance.toInt(hiddenToken2)]
                        = probability;
            });
        });
        projectionMatrix.forEach((hiddenToken, map) -> {
            map.forEach((visibleToken, probability) -> {
                this.projectionMatrix
                        [hiddenTokenTypeEnumInstance.toInt(hiddenToken)]
                        [visibleTokenTypeEnumInstance.toInt(visibleToken)]
                        = probability;
            });
        });
    }
    
    // getters & setters.
    /**
     * retrieve a specific HMM's transition matrix.
     * @return the HMM's transition matrix.
     */
    public double[][] getTransitionMatrix() {
        return this.transitionMatrix;
    }

    /**
     * set/modify an HMM's transition matrix.
     * @param newTransitionMatrix A new transition Matrix.
     */
    public void setTransitionMatrix(double[][] newTransitionMatrix) {
        this.transitionMatrix = newTransitionMatrix;
    }

    /**
     * retrieve a specific HMM's projection matrix.
     * @return the projection matrix.
     */
    public double[][] getProjectionMatrix() {
        return this.projectionMatrix;
    }
    
    /**
     * set/modify an HMM's projection matrix.
     * @param newProjectionMatrix A new projection matrix.
     */
    public void setProjectionMatrix(double[][] newProjectionMatrix) {
        this.projectionMatrix = newProjectionMatrix;
    }
    
    /**
     * retrieve a specific HMM's initialization matrix.
     * @return The initialization matrix.
     */
    public double[] getInitializationMatrix() { return this.initializationMatrix; }
   
    /**
     * set/modify an HMM's initialization matrix.
     * @param newInitializationMatrix A new initialization matrix.
     */
    public void setInitializationMatrix(double[] newInitializationMatrix) {
        this.initializationMatrix = newInitializationMatrix;
    }
    
    
    /** Calculate the probability of a specified hidden token sequence.
     * 
     * @param hiddenTokenSequence
     * @return The probability of the specified hidden token sequence.
     */
    public double probabilityOfHiddenTokenSequence(List<HiddenTokenType> hiddenTokenSequence) {
        Enumerable<HiddenTokenType> instance = this.hiddenTokenTypeEnumInstance;
        double res = this.initializationMatrix[instance.toInt(hiddenTokenSequence.get(0))];
        for(int i = 1; i < hiddenTokenSequence.size(); i++) {
            res *= this.transitionMatrix
                    [instance.toInt(hiddenTokenSequence.get(i - 1))]
                    [instance.toInt(hiddenTokenSequence.get(i))];
        }
        return res;
    }
    
    /**
     * Calculate the probability of a specified sequence of visible
     * tokens using the forward algorithm.
     * @param visibleTokenSequence
     * @return 
     */
    public double probabilityOfSequence(List<VisibleTokenType> visibleTokenSequence) {
        int sequenceSize = visibleTokenSequence.size();
        int hiddenTokenTypeSize = this.hiddenTokenTypeEnumInstance.size();
        
        double[][] F = new double[sequenceSize][hiddenTokenTypeSize];
        
        
        // initialization.
        F[0][0] = 0;
        VisibleTokenType firstToken = visibleTokenSequence.get(0);
        int firstTokenRepresentative = this.visibleTokenTypeEnumInstance.toInt(firstToken);
        for(int i = 0; i < hiddenTokenTypeSize; i++) {
            F[0][i] = this.initializationMatrix[i] * this.projectionMatrix[i][firstTokenRepresentative];
        }
        // recursion.
        for(int i = 1; i < sequenceSize; i++) {
            int observedToken = this.visibleTokenTypeEnumInstance.toInt(visibleTokenSequence.get(i));
            for(int j = 0; j < hiddenTokenTypeSize; j++) {
                F[i][j] = 0;
                for(int k = 0; k < hiddenTokenTypeSize; k++) {
                    F[i][j] += F[i-1][k] * this.transitionMatrix[k][j];
                }
                F[i][j] *= this.projectionMatrix[j][observedToken];
            }
        }
        // resolution.
        double res = 0;
        for(int i = 0; i < hiddenTokenTypeSize; i++) {
            res += F[sequenceSize-1][i];
        }
        
        return res;
    }
    
    /**
     * Calculate the most probable hidden token sequence for an observed sequence.
     * @param visibleTokenSequence
     * @return The guessed hidden token sequence.
     */
    public List<HiddenTokenType> mostProbableHiddenSequenceOf(
            List<VisibleTokenType> visibleTokenSequence
    ) {
        Enumerable<VisibleTokenType> visibleInstance = this.visibleTokenTypeEnumInstance;
        Enumerable<HiddenTokenType> hiddenInstance = this.hiddenTokenTypeEnumInstance;
        
        int visibleSequenceSize = visibleTokenSequence.size();
        int hiddenInstanceSize = hiddenInstance.size();
        double[][] T1 = new double[hiddenInstanceSize][visibleSequenceSize + 1];
        int[][] T2 = new int[hiddenInstanceSize][visibleSequenceSize + 1];
        int[] preres = new int[visibleSequenceSize];
        ArrayList<HiddenTokenType> res = new ArrayList<>();
        
        int firstVisibleTokenRepresentative = visibleInstance.toInt(visibleTokenSequence.get(0));
        int firstHiddenTokenRepresentative = 0;
        for(int i = 0; i < hiddenInstance.size(); i++) {
            T1[i][0] = this.initializationMatrix[i] * this.projectionMatrix[i][firstVisibleTokenRepresentative];
            T2[i][0] = -1;
        }
        for(int i = 1; i < visibleSequenceSize; i++) {
            for(int j = 0; j < hiddenInstance.size(); j++) {
                for(int k = 0; k < hiddenInstance.size(); k++) {
                    double P = T1[k][i-1] * this.transitionMatrix[k][j] * this.projectionMatrix[j][visibleInstance.toInt(visibleTokenSequence.get(i))];
                    if(P > T1[j][i]) {
                        T1[j][i] = P;
                        // this is the argmax operation.
                        // why does it feel extremely weird to me to implement them
                        T2[j][i] = k;
                    } 
                }
            }
        }
        int lastHiddenTokenRepresentative = 0;
        for(int i = 0; i < hiddenInstance.size(); i++) {
            if(T1[i][visibleSequenceSize - 1] > T1[lastHiddenTokenRepresentative][visibleSequenceSize - 1]){
                lastHiddenTokenRepresentative = i;
            }
        }
        preres[visibleSequenceSize - 1] = lastHiddenTokenRepresentative;
        for(int i = visibleSequenceSize - 1; i > 0; i--) {
            lastHiddenTokenRepresentative = T2[lastHiddenTokenRepresentative][i];
            preres[i - 1] = lastHiddenTokenRepresentative;
        }
        for(int i = 0; i < preres.length; i++) {
            res.add(hiddenInstance.fromInt(preres[i]));
        }
        return res;
    }
    
    /**
     * Calculate & set the initialization matrix from counts.
     * @param frequencyMatrix 
     */
    public void setInitializationMatrixFromFrequency(int[] frequencyMatrix) {
        frequencyMatrix = HMM.smooth(frequencyMatrix);
        int sum = 0;
        for(int count : frequencyMatrix) {
            sum += count;
        }
        double[] resInitializationMatrix = new double[frequencyMatrix.length];
        for(int i = 0; i < frequencyMatrix.length; i ++) {
            resInitializationMatrix[i]
                    = ((double)frequencyMatrix[i]) / ((double)sum);
        }
        this.initializationMatrix = resInitializationMatrix;
    }
    /**
     * Calculate & set the transition matrix from counts.
     * @param frequencyMatrix 
     */
    public void setTransitionMatrixFromFrequency(int[][] frequencyMatrix) {
        frequencyMatrix = HMM.smooth2D(frequencyMatrix);
        double[][] res = new double[frequencyMatrix.length][frequencyMatrix[0].length];
        // calculation.
        for(int i = 0; i < frequencyMatrix.length; i++) {
            int sum = 0; for(int j = 0; j < frequencyMatrix[i].length; j++) {
                sum += frequencyMatrix[i][j];
            }
            for(int j = 0; j < res[i].length; j++) {
                res[i][j] = ((double)frequencyMatrix[i][j]) / ((double)sum);
            }
        }
        this.transitionMatrix = res;
    }
    /**
     * Calculate & set the projection matrix from counts.
     * @param frequencyMatrix 
     */
    public void setProjectionMatrixFromFrequency(int[][] frequencyMatrix) {
        frequencyMatrix = HMM.smooth2D(frequencyMatrix);
        double[][] res = new double[frequencyMatrix.length][frequencyMatrix[0].length];
        // calculation.
        for(int i = 0; i < frequencyMatrix[0].length; i++) {
            int sum = 0; for(int j = 0; j < frequencyMatrix.length; j++) {
                sum += frequencyMatrix[j][i];
            }
            for(int j = 0; j < res.length; j++) {
                res[j][i] = ((double)frequencyMatrix[j][i]) / ((double)sum);
            }
        }
        this.projectionMatrix = res;
    }
    
    // debugging facilities.
    /**
     * Print the projection matrix in the stdout.
     */
    public void displayProjectionMatrix() {
        System.out.print("\t");
        for(int i = 0; i < this.projectionMatrix[0].length; i++) {
            System.out.print(this.visibleTokenTypeEnumInstance.fromInt(i)+"\t");
        }
        System.out.println();
        for(int i = 0; i < this.projectionMatrix.length; i++) {
            System.out.print(this.hiddenTokenTypeEnumInstance.fromInt(i)+"\t");
            for(int j = 0; j < this.projectionMatrix[i].length; j++) {
                System.out.print(this.projectionMatrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
    /**
     * Print the transition matrix in the stdout.
     */
    public void displayTransitionMatrix() {
        System.out.print("\t");
        for(String hiddenTokenName : this.hiddenTokenTypeEnumInstance.names()) {
            System.out.print(hiddenTokenName + "\t");
        }
        System.out.println();
        for(int i = 0; i < this.hiddenTokenTypeEnumInstance.size(); i++) {
            System.out.print(this.hiddenTokenTypeEnumInstance.fromInt(i) + "\t");
            for(int j = 0; j < this.hiddenTokenTypeEnumInstance.size(); j++) {
                System.out.print(this.transitionMatrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
