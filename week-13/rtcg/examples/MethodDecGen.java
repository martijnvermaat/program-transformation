import gnu.bytecode.*;
import java.io.*;


public class MethodDecGenerated {

    public static void main(String[] args) 
        throws IOException, NoSuchMethodException, IllegalAccessException, 
        java.lang.reflect.InvocationTargetException {

        ClassType co = GenSimpleClass();

        ClassType co1 = genclass |[
            public class MyClass1 { }
            ]|;

        genMethod(co1);

        // Output class file in human-readable format:
        //ClassTypeWriter.print(co, System.out, 0);

        // Output class file to array:
        byte[] classFile = co.writeToArray();

        // Load the class file from byte array into the JVM 
        Class ty = new ArrayClassLoader().loadClass("MyClass", classFile);

        // Get the method
        java.lang.reflect.Method m = ty.getMethod("MySimple", new Class[] { });

        // Call the method:    
        System.out.println(m.invoke(null, new Object[] { }));

        // Output class file to array:
        byte[] classFile1 = co1.writeToArray();

        // Load the class file from byte array into the JVM 
        Class ty1 = new ArrayClassLoader().loadClass("MyClass1", classFile1);

        // Get the method
        java.lang.reflect.Method m1 = ty1.getMethod("MySimple", new Class[] { });

        // Call the method:    
        System.out.println(m1.invoke(null, new Object[] { }));

    }


    public static ClassType GenSimpleClass() {

        return genclass |[
            public class MyClass {
                #genmethod |[
                    genMethod(thisClass);
                    ]|;
            }
            ]|;

    }

    public static void genMethod(ClassType thisClass) {
        genmethod |[
            public static int MySimple() {
                return 38;
            }
            ]|;
    }

}


// This is needed because defineClass is protected in java.lang.ClassLoader:
class ArrayClassLoader extends ClassLoader {
    public Class loadClass(String name, byte[] classFile) {
        return defineClass(name, classFile, 0, classFile.length);
    }
}
