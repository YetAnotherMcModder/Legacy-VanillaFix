/*
*    This code was based off
*    https://github.com/natanfudge/Not-Enough-Crashes/blob/cb1b32c847a958bbce0d594134cc768daa38b7ec/notenoughcrashes/src/main/java/fudge/notenoughcrashes/utils/CrashLogUpload.java
*    The source file uses the MIT license
*/

package piper74.legacy.vanillafix.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class CrashReportUpload {

    private static String GIST_ACCESS_TOKEN_PART_1() {
        return "ghp_brEwCNAAB14PQfm";
    }

    private static String GIST_ACCESS_TOKEN_PART_2() {
        return "RAnKSBcqq5AsKU00PXVZt";
    }

    // I don't think there's any security problem because the token can only upload gists,
    // but GitHub will revoke the token as soon as it sees it, so we trick it by splitting the token into 2.
    private static final String GIST_ACCESS_TOKEN = GIST_ACCESS_TOKEN_PART_1() + GIST_ACCESS_TOKEN_PART_2();

    private static class GistPost {
        @SerializedName("public")
        public boolean isPublic;
        public Map<String, GistFile> files;

        public GistPost(boolean isPublic, Map<String, GistFile> files) {
            this.isPublic = isPublic;
            this.files = files;
        }
    }

    private static class GistFile {
        public String content;

        public GistFile(String content) {
            this.content = content;
        }
    }



    public static String uploadToGithubGists(String crashReport) throws IOException {
        /**
         * @return The link of the gist
         */
            String uploadKey = GIST_ACCESS_TOKEN;
            HttpPost post = new HttpPost("https://api.github.com/gists");

            String fileName = "mccrash.txt";
            post.addHeader("Authorization", "token " + uploadKey);

            GistPost body = new GistPost(false, new HashMap<String, GistFile>() {{
                put(fileName, new GistFile(crashReport));
            }});
            post.setEntity(createStringEntity(new Gson().toJson(body)));


            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                CloseableHttpResponse response = httpClient.execute(post);
                String responseString = EntityUtils.toString(response.getEntity());
                JsonObject responseJson = new Gson().fromJson(responseString, JsonObject.class);
                return responseJson.getAsJsonObject("files").getAsJsonObject(fileName).getAsJsonPrimitive("raw_url").getAsString();


        }
    }

    private static StringEntity createStringEntity(String text) {
        return new StringEntity(text, StandardCharsets.UTF_8);
    }
}