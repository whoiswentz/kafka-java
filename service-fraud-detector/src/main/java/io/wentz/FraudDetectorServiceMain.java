package io.wentz;

public class FraudDetectorServiceMain {
    public static void main(String[] args) {
        new ServiceRunner<>(FraudDetectorService::new)
                .start(3);
    }
}
