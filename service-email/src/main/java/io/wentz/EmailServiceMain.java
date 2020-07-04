package io.wentz;

class EmailServiceMain {
    public static void main(String[] args) {
        new ServiceRunner<>(EmailService::new).start(3);
    }
}
