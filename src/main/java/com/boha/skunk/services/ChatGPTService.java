package com.boha.skunk.services;

import com.boha.skunk.data.chatgpt.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/*
Which of the following are companies? Dell, dSAA, Apple Inc, Sammy Sosa, Trees, IBM, Chase Manhattan, Frank Sinatra, Philadelphia,
OpenAI, Solly Sombra, Citi Bank, Massey Ferguson
 */
@Service
@RequiredArgsConstructor
public class ChatGPTService {
    @Value("${chatGPTKey}")
    private String chatGPTKey;
    static final Logger logger = Logger.getLogger(ChatGPTService.class.getSimpleName());
    static final String mm = "\uD83C\uDF6F ChatGPTService: \uD83C\uDF6F\uD83C\uDF6F\uD83C\uDF6F";
    private static final String API_URL =
            "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client;
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    @Autowired
    private FirebaseService firebaseService;

    public ChatGPTService() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Set the maximum time to establish a connection
                .readTimeout(600, TimeUnit.SECONDS) // Set the maximum time to read data from the server
                .writeTimeout(600, TimeUnit.SECONDS) // Set the maximum time to write data to the server
                .retryOnConnectionFailure(true)
                .build();
    }

    String prefix = "Pick out company names from the text and return list";
    String suffix = ". Return results in a list of strings";
    static final MediaType mediaType = MediaType.parse("application/json");


    private List<String> getCompanyNamesFromList(List<String> possibleNames) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String name : possibleNames) {
            sb.append(name).append(" ");
        }
        List<String> companies = new ArrayList<>();
        ChatGPTRequest cr = new ChatGPTRequest();
        buildChatRequest(sb.toString(), cr, "Extract names of possible commercial companies from the text. " +
                "Return list of json objects. Each result object should have 1 JSON field: companyName");
        Request r = getRequest(cr);
        String responseBody = makeTheCall(r);
        if (responseBody != null) {
            ChatGPTResponse chatGPTResponse = G.fromJson(responseBody, ChatGPTResponse.class);

            if (chatGPTResponse != null) {
                for (Choice choice : chatGPTResponse.getChoices()) {
                    if (choice.getMessage().getContent().contains("[")) {
                        JSONArray array = new JSONArray(choice.getMessage().getContent());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            companies.add(object.getString("companyName"));
                        }
                    }
                }

            }
            logger.info(mm + " \uD83C\uDF88 chatGPT found " + companies.size() +
                    " possible company names \uD83C\uDF88\uD83C\uDF88");
            for (String company : companies) {
                logger.info(mm + " company, maybe? " + company + " \uD83C\uDF88\uD83C\uDF88\uD83C\uDF88");
            }
        }
        return companies;
    }

    public List<String> getCompanyNamesFromText(String textFromWebsite, String requestId) throws IOException {
        logger.info(mm + "Getting chatGPT to extract possible companies from Website" +
                " .......... " + textFromWebsite.length() + " bytes from web page");
        List<String> companies = new ArrayList<>();
        if (textFromWebsite.isEmpty()) {
            return companies;
        }

        ChatGPTRequest cr = new ChatGPTRequest();
        buildChatRequest(textFromWebsite, cr, "Extract names of possible commercial companies from the textFromWebsite. " +
                "Return list of json objects. Each result object should have 1 JSON field: companyName. Exclude government and educational institutions");
        Request r = getRequest(cr);
        String responseBody = makeTheCall(r);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String isoString = sdf.format(new Date());
        if (responseBody != null) {
            try {
                ChatGPTResponse chatGPTResponse = G.fromJson(responseBody, ChatGPTResponse.class);
                if (chatGPTResponse != null) {
                    for (Choice choice : chatGPTResponse.getChoices()) {
                        if (choice.getMessage().getContent().contains("[")) {
                            JSONArray array = new JSONArray(choice.getMessage().getContent());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                companies.add(object.getString("companyName"));
                            }
                        }
                    }
                    chatGPTResponse.setRequestId(requestId);
                    chatGPTResponse.setDate(isoString);

                    //firebaseService.addChatGPTResponse(chatGPTResponse);
                    logger.info(mm + " companies from raw textFromWebsite: " + companies.size());
                }
            } catch (JsonSyntaxException | JSONException e) {
                logger.severe(mm + "\uD83D\uDD34\uD83D\uDD34\uD83D\uDD34" +
                        "Unable to process response from ChatGPT ... ");
                e.printStackTrace();
                return companies;
            }
            logger.info(mm + " \uD83C\uDF88\uD83C\uDF88\uD83C\uDF88 chatGPT found " + companies.size()
                    + " possible company names \uD83C\uDF88\uD83C\uDF88");

            for (String company : companies) {
                logger.info(mm + "getCompanyNamesFromText: ✅ ✅ \uD83C\uDF4E " +
                        "company: \uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E" + company);
            }
            logger.info(mm+"\n\n");
        }
        return companies;
    }

    @Nullable
    private String makeTheCall(Request request) throws IOException {
        logger.info(mm + " ............... makeTheCall for ChatGPT:  " +
                "\uD83D\uDC9A\uD83D\uDC9A\uD83D\uDC9A" +
                " Calling ChatGPT using OkHttpClient .......................  " +
                "\uD83D\uDC9A\uD83D\uDC9A");
        long start = System.currentTimeMillis();
        String responseBody;
        try (Response mResponse = client.newCall(request).execute()) {
            assert mResponse.body() != null;
            responseBody = mResponse.body().string();
        }
        if (responseBody.contains("error") || responseBody.contains("invalid_request_error")) {
            ChatGPTError error = G.fromJson(responseBody, ChatGPTError.class);
            logger.severe(mm + " \uD83D\uDD34 ERROR from chatGPT: "
                    + G.toJson(error) + " \uD83D\uDD34\uD83D\uDD34\uD83D\uDD34");
            return null;
        }

        logger.info(mm + " ChatGPT has responded!!  " +
                "response length: " + responseBody.length() +
                " bytes. \uD83D\uDC9A \uD83D\uDC9A \uD83D\uDC9A");
        printElapsed(start);
        return responseBody;
    }
    private static void printElapsed(long startTime) {
        //
        long endTime = System.currentTimeMillis();
        long elapsedTimeMillis = endTime - startTime;
        double elapsedTimeMinutes = elapsedTimeMillis / 1000.0 / 60;
        double elapsedTimeSeconds = elapsedTimeMillis / 1000.0;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String minutes = decimalFormat.format(elapsedTimeMinutes);
        String seconds = decimalFormat.format(elapsedTimeSeconds);
//10210 option 1 tekom technical
        logger.info(mm+"\uD83C\uDF4A\uD83C\uDF4A\uD83C\uDF4A ChatGPT complete: "
                + minutes + " elapsed minutes;  " + seconds + " seconds " +
                "\uD83E\uDD4F \uD83D\uDD35\uD83D\uDD35");
    }
    private static void buildChatRequest(String string, ChatGPTRequest cr, String prompt) {
        cr.setMessages(new ArrayList<>());
        cr.setModel("gpt-4");
        cr.getMessages().add(new Message("system", prompt));
        cr.getMessages().add(new Message("user", string));
    }

    private Request getRequest(ChatGPTRequest cr) {
        RequestBody body = RequestBody.create(mediaType,
                G.toJson(cr));

        return new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + chatGPTKey)
                .addHeader("Content-Type", "application/json")
                .build();
    }

}
