package com.hhplus.io.common.support.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PararellInsertUtil {


    private static final String URL = "jdbc:mysql://localhost:3306/concert"; // DB URL
    private static final String USER = "hhplus";
    private static final String PASSWORD = "hhplus";
    private static final int TOTAL_RECORDS = 5000000; // 총 1천만 건
    private static final int BATCH_SIZE = 100000; // 배치 크기
    private static final int THREAD_COUNT = 10; // 병렬 스레드 개수

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int startId = i * (TOTAL_RECORDS / THREAD_COUNT) + 1;
            final int endId = (i + 1) * (TOTAL_RECORDS / THREAD_COUNT);
            executor.submit(() -> insertConcertBatch(startId, endId));
        }

        executor.shutdown();
    }

    private static void insertConcertBatch(int startId, int endId) {
        String insertSQL = "INSERT INTO amount (amount_id, user_id, amount, created_at, updated_at) " +
                "VALUES (?, ?, 20000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

                for (int i = startId; i <= endId; i++) {
                    pstmt.setInt(1, 5000000+i);
                    pstmt.setInt(2, 5000000+i);
//                    pstmt.setString(2, "홍" + i);

                    pstmt.addBatch();

                    if (i % BATCH_SIZE == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                        System.out.println("Committed batch at ID: " + i);
                    }
                }

                pstmt.executeBatch(); // 남은 데이터 커밋
                conn.commit();
                System.out.println("Finished inserting from " + startId + " to " + endId);

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
