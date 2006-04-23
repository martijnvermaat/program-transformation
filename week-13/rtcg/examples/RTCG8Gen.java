// File RTCG8.java --- sparse matrices
// sestoft@dina.kvl.dk * 2002-09-19

// Using the gnu.bytecode package from http://www.gnu.org/software/kawa

// A matrix A is an array of arrays (rows) of doubles, so A[i][j] is
// row i column j.

// We consider multiplications A*B where B is a sparse matrix, and
// for given B generate code that computes A*B when given a matrix A.

import gnu.bytecode.*;
import java.io.*;               // IOException
import java.util.*;             // Random, ArrayList etc

public class RTCG8Gen {
  public static void main(String[] args) 
    throws IOException, NoSuchMethodException, IllegalAccessException, 
           java.lang.reflect.InvocationTargetException {
    final int dim1 = Integer.parseInt(args[0]),
      dim2 = Integer.parseInt(args[1]),
      dim3 = Integer.parseInt(args[2]),
      nonzero = Integer.parseInt(args[3]),
      count = Integer.parseInt(args[4]);

    // Generate some random matrices 
    final double[][] A = randomSparse(dim1, dim2, dim1*dim2),
        B = randomSparse(dim2, dim3, nonzero),
        R1 = new double[dim1][dim3],
        R2 = new double[dim1][dim3],
        R3 = new double[dim1][dim3],
        R4 = new double[dim1][dim3];

    // Generate a specialized sparse multiplication method
    ClassType co = genclass |[
        public class MyClass { }
        ]|;

    Class ty = null;

    // Build: public static void sparseMultB(double[][] A, double[][] R)
    // and possibly additional copies (for timing)
    {
      final Timer t = new Timer();
      final SparseMatrix sparseB = new SparseMatrix(B);
      sparseMultGen(co, "sparseMultB", dim1, dim2, sparseB);
      // If there are 6 or more arguments, build the method repeatedly
      final int methodCount = args.length >= 6 ? Integer.parseInt(args[5]) : 1;
      for (int i=methodCount-1; i>0; i--)
          sparseMultGen(co, "sparseMultB"+i, dim1, dim2, sparseB);
      // Output class file to array:
      byte[] classFile = co.writeToArray();
      // Load the class file into the JVM 
      ty = new ArrayClassLoader().loadClass("MyClass", classFile);
      System.out.println("Generating " + methodCount 
			 + " copies of sparseMultB: " + t.Check() + " sec");
    }
    { 
      Timer t = new Timer();
      for (int i=count; i>0; i--)
        matrixMult(A, B, R1);
      System.out.println("matrixMult: " + t.Check() + " sec");
    }
    { 
      Timer t = new Timer();
      for (int i=count; i>0; i--)
        sparseMult(A, B, R2);
      System.out.println("sparseMult: " + t.Check() + " sec");
    }
    { 
      Timer t = new Timer();
      SparseMatrix sparseB = new SparseMatrix(B);
      for (int i=count; i>0; i--)
        sparseMult(A, sparseB, R3);
      System.out.println("sparseMult, two-phase: " + t.Check() + " sec");
    }
    {
      java.lang.reflect.Method m = 
        ty.getMethod("sparseMultB", 
                     new Class[] { double[][].class, double[][].class }); 
      Timer t = new Timer();
      for (int i=count; i>0; i--)
        m.invoke(null, new Object[] { A, R4 });
      System.out.println("Generated sparseMultB: " + t.Check() + " sec");
    }
    { 
      Timer t = new Timer();
      for (int i=count; i>0; i--)
        matrixMult(A, B, R1);
      System.out.println("matrixMult: " + t.Check() + " sec");
    }
    System.out.println("R1 == R2 is " + equal(R1, R2));
    System.out.println("R2 == R3 is " + equal(R2, R3));
    System.out.println("R3 == R4 is " + equal(R3, R4));
  }

  // Compute A*B in R 
  // Assume A, B and R are rectangular and non-empty

  public static void matrixMult(double[][] A, double[][] B, double[][] R) {
    final int aRows = A.length, aCols = A[0].length,
      bRows = B.length, bCols = B[0].length,
      rRows = R.length, rCols = R[0].length;
    if (aCols != bRows || aRows != rRows || bCols != rCols)
      throw new Error("Matrix dimension mismatch");
    else {
      for (int i=0; i<rRows; i++)
        for (int j=0; j<rCols; j++) {
          double sum = 0.0;
          for (int k=0; k<aCols; k++)
            sum += A[i][k] * B[k][j];
          R[i][j] = sum;
        }
    }
  }

  // Compute A*B in R in two steps, first building a sparse
  // representation of B (an array of lists of nonzero column elements)

  // Assume A, B and R are rectangular and non-empty

  public static void sparseMult(double[][] A, double[][] B, double[][] R) {
    final int aRows = A.length, aCols = A[0].length,
      bRows = B.length, bCols = B[0].length,
      rRows = R.length, rCols = R[0].length;
    if (aCols != bRows || aRows != rRows || bCols != rCols)
      throw new Error("Matrix dimension mismatch");
    else {
      SparseMatrix sparseB = new SparseMatrix(B);
      sparseMult(A, sparseB, R);
    }
  }

  // Compute A*B in R, where B is represented as a SparseMatrix
  // Assume A, B and R conform, and B and R are rectangular and non-empty

  private static void sparseMult(double[][] A, SparseMatrix B, double[][] R) {
    final int rRows = R.length, rCols = R[0].length;
    for (int i=0; i<rRows; i++) {
      final double[] Ai = A[i];
      final double[] Ri = R[i];
      for (int j=0; j<rCols; j++) {
        double sum = 0.0;
        Iterator iter = B.getCol(j).iterator();
        while (iter.hasNext()) {
          final NonZero nz = (NonZero)iter.next();
          sum += Ai[nz.k] * nz.Bkj;
        }
        Ri[j] = sum;
      }
    }
  }

  // Given a SparseMatrix B, generate code to compute A*B in R for any A
  // Build: public static void sparseMultB(double[][] A, double[][] R)

    private static void sparseMultGen(ClassType thisClass, String name,
				    int aRows, int aCols, SparseMatrix B) {

        genmethod |[
            public static void sparseMultB(double[][] A, double[][] R) {

                double[] Ai;
                double[] Ri;
                int i;

                i = 0;

                do {

                    Ai = A[i];
                    Ri = R[i];

                    #genbstms |[

                        for (int j=0; j<B.cols; j++) {

                            genbstms |[
                                double sum;
                                sum = 0.0;
                                ]|;

                            Iterator iter = B.getCol(j).iterator();
                            while (iter.hasNext()) {
                                final NonZero nz = (NonZero)iter.next();
                                genbstms |[
                                    sum = sum + #double[nz.Bkj] * Ai[ #int[nz.k] ];
                                    ]|;
                            }

                            genbstms |[
                                Ri[ #int[j] ] = sum;
                                ]|;

                        }

                        ]|;

                    i = i + 1;

                } while (i < Rows);

            }
            ]|;

    }

  // A SparseMatrix has a dimension, and an array of the NonZeros in
  // each of B's columns

  private static class SparseMatrix {
    final int rows, cols;
    final ArrayList[] /* of NonZero */ nonzeros;

    public SparseMatrix(double[][] B) {
      rows = B.length; cols = B[0].length;
      nonzeros = new ArrayList[cols];
      for (int j=0; j<cols; j++) {
        nonzeros[j] = new ArrayList();
        for (int k=0; k<rows; k++) 
          if (B[k][j] != 0.0)
            nonzeros[j].add(new NonZero(k, B[k][j]));
      }
    }
    
    public ArrayList /* of NonZero */ getCol(int j) {
      return nonzeros[j];
    }
  }

  // A pair of a row number k and a non-zero element B[k][-]

  private static class NonZero {
    final int k;
    final double Bkj;

    public NonZero(int k, double Bkj) {
      this.k = k; this.Bkj = Bkj;
    }
  }
    
  // Generate a rectangular matrix with at most n non-zero elements,

  public static double[][] randomSparse(int rows, int cols, int n) {
    final Random rnd = new Random();
    final double[][] R = new double[rows][cols];      // All zeros initially
    for (int k=0; k<n; k++) {
      final int i = rnd.nextInt(rows), j = rnd.nextInt(cols);
      R[i][j] = rnd.nextDouble();
    }
    return R;
  }

  // Compare matrices

  public static boolean equal(double[][] A, double[][] B) {
    final int aRows = A.length, aCols = A[0].length,
      bRows = B.length, bCols = B[0].length;
    if (aCols != bCols || aRows != bRows)
      return false;
    else {
      for (int i=0; i<aRows; i++)
        for (int j=0; j<aCols; j++) 
          if (A[i][j] != B[i][j])
            return false;
      return true;
    }
  }
}

// This is needed because defineClass is protected in java.lang.ClassLoader:

class ArrayClassLoader extends ClassLoader {
  public Class loadClass(String name, byte[] classFile) {
    return defineClass(name, classFile, 0, classFile.length);
  }
}

// Crude timing utility ----------------------------------------
   
class Timer {
  private long start;

  public Timer() {
    start = System.currentTimeMillis();
  }

  public double Check() {
    return (System.currentTimeMillis()-start)/1000.0;
  }
}
