import io.github.classgraph.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.wings.prpc.remote.DefaultExecutor;

/**
 * Created by sp00x on 8/29/2019.
 * Project: wings-testing
 */
public class ClassGraphTest {


    @Test
    void name1() throws Exception {
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
//                .enableExternalClasses()
                .enableInterClassDependencies()
//                .verbose()
                .scan()) {
//            System.out.println(scanResult.getClasspath());
//            System.out.println(scanResult.getAllAnnotations());
//            scanResult.getAllStandardClasses().forEach(System.out::println);
//            scanResult.get
//            ClassInfo classInfo = scanResult.getClassInfo("org.wings.prpc.remote.TestClassDependency");
//            System.out.println(classInfo.getInnerClasses());
//            ClassInfoList classDependencies = classInfo.getClassDependencies();
//            classDependencies.forEach(System.out::println);
//            System.out.println();
            ClassInfo classInfo = scanResult.getClassInfo(Mockito.class.getName());
            System.out.println(classInfo.getResource().getPath());
//            File classpathElementFile = classInfo.getClasspathElementFile();
//            System.out.println(classpathElementFile);
//
//            Tika tika = new Tika();
//            String detect1 = tika.detect(classpathElementFile.getName());
//            System.out.println(detect1);
//            String detect2 = tika.detect(classInfo.getResource().getPath());
//            System.out.println(detect2);

//            DefaultDetector detector = new DefaultDetector();
//            Metadata metadata = new Metadata();
//            MediaType mediaType = detector.detect(new FileInputStream(classpathElementFile), metadata);
//            System.out.println(mediaType);


            String packageName = DefaultExecutor.class.getPackage().getName();
            System.out.println("Classes of package: " + packageName);
            PackageInfo packageInfo = scanResult.getPackageInfo(packageName);
            ClassInfoList classInfo1 = packageInfo.getClassInfo();
            classInfo1.forEach(System.out::println);
            System.out.println();
        }
    }

    @Test
    void name2() {
        long start = System.currentTimeMillis();
        try (ScanResult scanResult = new ClassGraph()
                .enableInterClassDependencies()
                .scan()) {
            long x = System.currentTimeMillis() - start;
            System.out.println(x);
            scanResult.getResourcesWithPath("org/wings/prpc/remote/dependency/testing/res1.txt")
                    .stream()
                    .map(c -> c.getClasspathElementFile())
                    .forEach(System.out::println);
            System.out.println(System.currentTimeMillis() - start - x);
        }
    }
}
