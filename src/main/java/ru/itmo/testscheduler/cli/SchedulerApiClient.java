package ru.itmo.testscheduler.cli;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class SchedulerApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final String baseUrl;

    public SchedulerApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public String schedule(Object request, String strategy, String format) throws Exception {
        String json = mapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/schedule?strategy=" + strategy + "&format=" + format))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
                .body();
    }

    public void uploadReport(String runId, Path file) throws Exception {

        String boundary = "----Boundary";

        var body = new StringBuilder();
        body.append("--").append(boundary).append("\r\n");
        body.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                .append(file.getFileName())
                .append("\"\r\n");
        body.append("Content-Type: application/xml\r\n\r\n");

        String prefix = body.toString();
        byte[] fileBytes = Files.readAllBytes(file);

        byte[] suffix = ("\r\n--" + boundary + "--\r\n").getBytes();

        byte[] requestBody = concat(prefix.getBytes(), fileBytes, suffix);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/reports?runId=" + runId))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private byte[] concat(byte[] a, byte[] b, byte[] c) {
        byte[] result = new byte[a.length + b.length + c.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        System.arraycopy(c, 0, result, a.length + b.length, c.length);
        return result;
    }
}