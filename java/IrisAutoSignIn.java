import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

public class IrisAutoSignIn {
    private final String secretKey;
    private final String rootUrl;

    public IrisAutoSignIn(String secretKey, boolean isProduction) {
        this.secretKey = secretKey;
        this.rootUrl = isProduction ? "https://iris.opynhealth.com" : "https://test-iris.opynhealth.com";
    }

    public String embedUrl(String clientId, String groupId, String memberId, Date expiresAt) throws Exception {
        return urlBuild(rootUrl + "/embed", clientId, groupId, memberId, expiresAt);
    }

    public String autoSignInUrl(String clientId, String groupId, String memberId, Date expiresAt) throws Exception {
        return urlBuild(rootUrl + "/autoSignIn", clientId, groupId, memberId, expiresAt);
    }

    private String urlBuild(String endpoint, String clientId, String groupId, String memberId, Date expiresAt) throws Exception {
        String expiresAtText = formatIso8601(expiresAt);
        String message = clientId + ":" + groupId + ":" + memberId + ":" + expiresAtText;
        String signature = hmacSha256(message, secretKey);

        // Construct URL with parameters
        return endpoint + "?clientId=" + urlEncode(clientId) +
                "&groupId=" + urlEncode(groupId) +
                "&memberId=" + urlEncode(memberId) +
                "&expires=" + urlEncode(expiresAtText) +
                "&signature=" + urlEncode(signature);
    }

    private String hmacSha256(String message, String secret) throws SignatureException {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new SignatureException("Error generating HMAC-SHA256 signature", e);
        }
    }

    private String formatIso8601(Date date) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return isoFormat.format(date);
    }

    private String urlEncode(String value) throws Exception {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    public static void main(String[] args) {
        try {
            String secretKey = "SECRET_KEY";
            String clientId = "CLIENT_ID";
            String groupId = "GROUP_ID";
            String memberId = "MEMBER_ID";
            boolean isProduction = false;
            Date expiresAt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 24 hours from now

            IrisAutoSignIn asi = new IrisAutoSignIn(secretKey, isProduction);

            System.out.println("## Java Example ##");
            System.out.println("Auto sign-in URL:");
            System.out.println(asi.autoSignInUrl(clientId, groupId, memberId, expiresAt));
            System.out.println("");

            System.out.println("Embedded URL:");
            System.out.println(asi.embedUrl(clientId, groupId, memberId, expiresAt));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
