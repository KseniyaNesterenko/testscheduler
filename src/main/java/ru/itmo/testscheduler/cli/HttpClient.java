package ru.itmo.testscheduler.cli;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpClient {

    public static String post(String urlStr, String body) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
        }

        int code = conn.getResponseCode();

        InputStream is = (code >= 200 && code < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        return new String(is.readAllBytes());
    }

    public static void uploadFile(String urlStr, Path file) throws Exception {
        String boundary = "----Boundary";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream os = conn.getOutputStream()) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(os), true);

            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                    .append(file.getFileName().toString())
                    .append("\"\r\n");
            writer.append("Content-Type: application/octet-stream\r\n\r\n").flush();

            Files.copy(file, os);
            os.flush();

            writer.append("\r\n--").append(boundary).append("--\r\n").flush();
        }

        if (conn.getResponseCode() >= 300) {
            throw new RuntimeException("Upload failed: " + conn.getResponseMessage());
        }
    }
}