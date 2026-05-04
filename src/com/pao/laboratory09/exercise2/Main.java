package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();


        File outputFile = new File(OUTPUT_FILE);
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());


                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array());


                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array());


                byte[] dataBytes = new byte[10];
                Arrays.fill(dataBytes, (byte) ' ');
                byte[] strBytes = data.getBytes("US-ASCII");
                System.arraycopy(strBytes, 0, dataBytes, 0, Math.min(strBytes.length, 10));
                dos.write(dataBytes);


                dos.writeByte(tip == TipTranzactie.CREDIT ? 0 : 1);
                dos.writeByte(0);
                dos.write(new byte[8]);
            }
        }
        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNext()) {
                String command = scanner.next();

                if ("READ".equals(command)) {
                    int idx = scanner.nextInt();
                    readAndPrintRecord(raf, idx);
                } else if ("UPDATE".equals(command)) {
                    int idx = scanner.nextInt();
                    String statusStr = scanner.next();
                    byte statusByte = 0;
                    if ("PROCESSED".equals(statusStr)) {
                        statusByte = 1;
                    } else if ("REJECTED".equals(statusStr)) {
                        statusByte = 2;
                    } else if ("PENDING".equals(statusStr)) {
                        statusByte = 0;
                    }

                    raf.seek(idx * (long) RECORD_SIZE + 23);
                    raf.write(statusByte);
                    System.out.println("Updated [" + idx + "]: " + statusStr);
                } else if ("PRINT_ALL".equals(command)) {
                    long length = raf.length();
                    int numRecords = (int) (length / RECORD_SIZE);
                    for (int i = 0; i < numRecords; i++) {
                        readAndPrintRecord(raf, i);
                    }
                }
            }
        }
    }

    private static void readAndPrintRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek(idx * (long) RECORD_SIZE);
        byte[] record = new byte[RECORD_SIZE];
        int bytesRead = raf.read(record);
        if (bytesRead != RECORD_SIZE) {
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);

        int id = buffer.getInt();
        double suma = buffer.getDouble();
        
        byte[] dataBytes = new byte[10];
        buffer.get(dataBytes);
        String data = new String(dataBytes, "US-ASCII").trim();
        
        byte tipByte = buffer.get();
        String tip = tipByte == 0 ? "CREDIT" : "DEBIT";
        
        byte statusByte = buffer.get();
        String status = "PENDING";
        if (statusByte == 1) {
            status = "PROCESSED";
        } else if (statusByte == 2) {
            status = "REJECTED";
        }

        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s\n",
                idx, id, data, tip, suma, status);
    }
}
