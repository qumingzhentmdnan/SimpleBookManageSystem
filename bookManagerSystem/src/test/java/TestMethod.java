import org.junit.jupiter.api.Test;

public class TestMethod {
    @Test
    public void test(){
        String str="fujianz";
        System.out.println(str.matches("^[a-zA-Z0-9]{6,16}$"));
    }
}