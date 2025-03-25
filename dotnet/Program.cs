using System;
using System.Security.Cryptography;
using System.Text;

public class IrisAutoSignIn
{
    private readonly string rootUrl = "https://iris-test.opynhealth.com";
    private readonly string secretKey;

    public IrisAutoSignIn(string secretKey, bool isProduction)
    {
        this.secretKey = secretKey;
        if(isProduction)
            rootUrl = "https://iris.opynhealth.com";
    }

    /// <summary>
    /// Generate the embed URL for the given parameters
    /// </summary>
    public string EmbedUrl(string clientId, string groupId, string memberId, DateTime expiresAt)
    {
        return UrlBuild($"{rootUrl}/embed", clientId, groupId, memberId, expiresAt);
    }

    /// <summary>
    /// Generate the auto sign-in URL for the given parameters
    /// </summary>
    public string SignedUrl(string clientId, string groupId, string memberId, DateTime expiresAt)
    {
        return UrlBuild($"{rootUrl}/autoSignIn", clientId, groupId, memberId, expiresAt);
    }

    private string UrlBuild(string endpoint, string clientId, string groupId, string memberId, DateTime expiresAt)
    {
        string expiresAtText = $"{expiresAt.ToUniversalTime():O}";
        string message = $"{clientId}:{groupId}:{memberId}:{expiresAtText}";
        using var hmac = new HMACSHA256(Encoding.UTF8.GetBytes(secretKey));
        byte[] hash = hmac.ComputeHash(Encoding.UTF8.GetBytes(message));
        string signature = Convert.ToBase64String(hash);

        string result = $"{endpoint}?clientId={clientId}&groupId={groupId}" + 
                        $"&memberId={memberId}&expires={expiresAtText}&signature={signature}";

        return result;
    }
}

public class Program
{
    public static void Main()
    {
        string secretKey = "SECRET_KEY";
        string clientId = "CLIENT_ID";
        string groupId = "GROUP_ID"; // group ID or group code
        string memberId = "MEMBER_ID";
        bool isProduction = false;
        DateTime expiresAt = DateTime.UtcNow.AddHours(24);

        var asi = new IrisAutoSignIn(secretKey, isProduction);

        Console.WriteLine("Auto sign-in URL:");
        string signedUrl = asi.SignedUrl(clientId, groupId, memberId, expiresAt);
        Console.WriteLine(signedUrl);
        Console.WriteLine("");

        Console.WriteLine("Embedded URL:");
        string embedUrl = asi.EmbedUrl(clientId, groupId, memberId, expiresAt);
        Console.WriteLine(embedUrl);
    }
}
