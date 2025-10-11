import java.net.URL;

public class TestResourceLoading {
    public static void main(String[] args) {
        // Test if we can find the FXML file
        URL resource = TestResourceLoading.class.getResource("/org/example/quizzapp/view/results-view.fxml");
        System.out.println("Resource found: " + resource);
        
        // Test relative path
        URL resource2 = TestResourceLoading.class.getResource("view/results-view.fxml");
        System.out.println("Relative resource found: " + resource2);
    }
}
