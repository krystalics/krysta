import org.junit.Test;

public class CLassScannerTest {
    @Test
    public void scan(){
        //类路径下的包(external libraries下)，java目录和test的java下的包可以扫描,
        ClassScanner.getClazzFromPkg("junit");
        ClassScanner.getClazzFromPkg("classreader");  //注意在test中如果有同名的包也会扫描

    }
}
