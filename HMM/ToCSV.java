/**
 * To convert arrays into CSV using LF as the newline pattern.
 * @author bctnry
 */
public class ToCSV {
    public static String fromDouble2D(double[][] array) {
        StringBuilder resStringBuilder = new StringBuilder();
        for (double[] array1 : array) {
            for (int j = 0; j < array1.length; j++) {
                resStringBuilder.append(array1[j]).append(j == array1.length - 1 ? "\n" : ",");
            }
        }
        return resStringBuilder.toString();
    }
    public static String fromDouble1D(double[] array) {
        StringBuilder resStringBuilder = new StringBuilder();
        for(int i = 0; i < array.length; i++) {
            resStringBuilder.append(array[i])
                    .append(i == array.length - 1? "\n" : ",");
        }
        return resStringBuilder.toString();
    }
    public static String fromInt1D(int[] array) {
        StringBuilder resStringBuilder = new StringBuilder();
        for(int i = 0; i < array.length; i++) {
            resStringBuilder.append(array[i])
                    .append(i == array.length - 1? "\n" : ",");
        }
        return resStringBuilder.toString();
    }
}
