package io.wentz;

public class ReadingReportMain {
    public static void main(String[] args) {
        new ServiceRunner<>(ReadingReportService::new).start(3);
    }
}
