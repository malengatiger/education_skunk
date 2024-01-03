package com.boha.skunk.util;

import com.boha.skunk.data.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.CDL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileToUsers {
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = LoggerFactory.getLogger(FileToUsers.class);

    public static List<User> getUsersFromJSONFile(File file) {
        List<User> users = new ArrayList<>();

        try {
            Path filePath = Path.of(file.getPath());
            String json = Files.readString(filePath);
            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            users = G.fromJson(json, listType);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static List<User> getUsersFromCSVFile(File file) throws IOException {
        List<User> users;
        try (FileInputStream is = new FileInputStream(file)) {
            String csv = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            String json = CDL.toJSONArray(csv).toString(2);
            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            users = G.fromJson(json, listType);

//                Files.write(Path.of("files/people2.json"), json.getBytes());
        }
        logger.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E " +
                "User objects created from csv: " + users.size());
        return users;
    }
}
