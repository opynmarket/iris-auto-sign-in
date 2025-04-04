
import hmac
import hashlib
import base64
from datetime import datetime, timedelta
from urllib.parse import urlencode

class IrisAutoSignIn:
    def __init__(self, secret_key, is_production=False):
        self.secret_key = secret_key
        self.root_url = "https://iris.opynhealth.com" if is_production else "https://test-iris.opynhealth.com"

    def embed_url(self, client_id, group_id, member_id, expires_at):
        return self._url_build(f"{self.root_url}/embed", client_id, group_id, member_id, expires_at)

    def auto_sign_in_url(self, client_id, group_id, member_id, expires_at):
        return self._url_build(f"{self.root_url}/autoSignIn", client_id, group_id, member_id, expires_at)

    def _url_build(self, endpoint, client_id, group_id, member_id, expires_at):
        expires_at_text = expires_at.isoformat()
        message = f"{client_id}:{group_id}:{member_id}:{expires_at_text}"
        
        # Create HMAC-SHA256 signature
        hmac_digest = hmac.new(self.secret_key.encode(), message.encode(), hashlib.sha256).digest()
        signature = base64.b64encode(hmac_digest).decode()

        # Construct the full URL with parameters
        params = {
            "clientId": client_id,
            "groupId": group_id,
            "memberId": member_id,
            "expires": expires_at_text,
            "signature": signature
        }

        return f"{endpoint}?{urlencode(params)}"

if __name__ == "__main__":
    secret_key = "SECRET_KEY"
    client_id = "CLIENT_ID"
    group_id = "GROUP_ID"  # Group ID or group code
    member_id = "MEMBER_ID"
    is_production = False
    expires_at = datetime.utcnow() + timedelta(hours=24)

    asi = IrisAutoSignIn(secret_key, is_production)

    print("## Python Example ##")
    print("Auto sign-in URL")
    auto_sign_in_url = asi.auto_sign_in_url(client_id, group_id, member_id, expires_at)
    print(auto_sign_in_url)
    print("")

    print("Embedded URL:")
    embed_url = asi.embed_url(client_id, group_id, member_id, expires_at)
    print(embed_url)
