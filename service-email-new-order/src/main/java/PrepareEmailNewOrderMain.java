import io.wentz.ServiceRunner;

public class PrepareEmailNewOrderMain {
    public static void main(String[] args) {
        new ServiceRunner<>(PrepareEmailNewOrderService::new)
                .start(3);
    }
}
