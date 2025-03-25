const crypto = require("crypto");
const { URL, URLSearchParams } = require("url");

class IrisAutoSignIn {
  constructor(secretKey, isProduction = false) {
    this.secretKey = secretKey;
    this.rootUrl = isProduction
      ? "https://iris.opynhealth.com"
      : "https://iris-test.opynhealth.com";
  }

  embedUrl(clientId, groupId, memberId, expiresAt) {
    return this._urlBuild(`${this.rootUrl}/embed`, clientId, groupId, memberId, expiresAt);
  }

  autoSignInUrl(clientId, groupId, memberId, expiresAt) {
    return this._urlBuild(`${this.rootUrl}/autoSignIn`, clientId, groupId, memberId, expiresAt);
  }

  _urlBuild(endpoint, clientId, groupId, memberId, expiresAt) {
    const expiresAtText = expiresAt.toISOString();
    const message = `${clientId}:${groupId}:${memberId}:${expiresAtText}`;

    // Create HMAC-SHA256 signature
    const hmac = crypto.createHmac("sha256", this.secretKey);
    hmac.update(message);
    const signature = hmac.digest("base64");

    // Construct the full URL with parameters
    const params = new URLSearchParams({
      clientId,
      groupId,
      memberId,
      expires: expiresAtText,
      signature,
    });

    return `${endpoint}?${params.toString()}`;
  }
}

// Example usage
const secretKey = "SECRET_KEY";
const clientId = "CLIENT_ID";
const groupId = "GROUP_ID";
const memberId = "MEMBER_ID";
const isProduction = false;
const expiresAt = new Date(Date.now() + 24 * 60 * 60 * 1000); // 24 hours from now

const asi = new IrisAutoSignIn(secretKey, isProduction);

console.log("Auto sign-in URL:");
console.log(asi.autoSignInUrl(clientId, groupId, memberId, expiresAt));
console.log("");

console.log("Embedded URL:");
console.log(asi.embedUrl(clientId, groupId, memberId, expiresAt));
