
# Iris Automatic Sign-In (ASI)

This project provides example code for generating automatic sign-In (ASI) 
embedded and auto sign-in URLs for the Opyn Iris platform. These examples
demonstrate how to securely integrate with the platform using HMAC 
authentication.

- Auto sign-in URL: Use this URL to navigate a member directly into the 
  Iris platform, fully authenticated
- Embedded URL: Use this URL to embed Iris within your web portal

## Table of Contents

- [Prerequisites](#prerequisites)
- [Examples](#examples)
- [Build Auto Sign-In or Embed URL](#build-auto-sign-in-or-embed-url)
- [Build iframe](#build-iframe)
- [Support](#support)

## Prerequisites

- Access to an Opyn Iris account with the required credentials 
  (`secretKey`, `clientId`, etc.). Contact your Opyn account manager for 
  these details.
- A development environment for one of the supported programming languages:
  - [.NET](https://dotnet.microsoft.com/en-us/download)
  - [Java](https://www.java.com/en/download/)
  - [Python](https://www.python.org/downloads/)
  - [Node.js](https://nodejs.org/en/)
- Basic knowledge of:
  - [HMAC authentication](https://en.wikipedia.org/wiki/HMAC)
  - [ISO 8601 date formatting](https://en.wikipedia.org/wiki/ISO_8601)

## Examples

- Review the folder (java, python, node, etc) with the example code that 
  matches your development environment.  
- Run the application. You will see output like this: 

```text
Auto sign-in URL:
https://test-iris.opynhealth.com/autoSignIn?clientId=CLIENT_ID&groupId=GROUP_ID&memberId=MEMBER_ID&expires=2025-03-22T15:26:52.8164580Z&signature=ErVtH/2PbfzMMrApGQn39j2DlaFixFGFu/bGAgWNX2o=

Embedded URL:
https://test-iris.opynhealth.com/embed?clientId=CLIENT_ID&groupId=GROUP_ID&memberId=MEMBER_ID&expires=2025-03-22T15:26:52.8164580Z&signature=ErVtH/2PbfzMMrApGQn39j2DlaFixFGFu/bGAgWNX2o=
```

## Build Auto Sign-In or Embed URL

To generate the URLs, configure the following variables:

- `secretKey`: The secret key used for signing the URLs. This will be provided 
  by your Opyn account manager.  
  > **Security Note:** Never expose your `secretKey` in public repositories 
    or logs. Treat it as sensitive information.
- `clientId`: The client ID for the ASI integration. This will be provided by 
  your Opyn account manager.
- `groupId`: The group ID or group code for the member accessing the application.
- `memberId`: The member ID.
- `isProduction`: A boolean flag to toggle between production and test environments.
- `expiresAt`: The expiration date and time for the generated URLs in ISO 8601 format.

### Notes

- The application defaults to the test environment. Set `isProduction` to `true` to switch to the production environment.
- Ensure that the `secretKey` is kept secure and not exposed in public repositories.

## Build iframe

To add the Opyn Iris member component to your web page, set the URL you have 
created above to the value of an iframe `src` attribute. You can adjust 
the component's size with the iframe's height and width attributes. See the 
example below, as well as the full list of all iframe options.  Adjust 
the iframe's dimensions as needed:

```html
<iframe
  width="450"
  height="250"
  frameborder="0"
  style="border:0"
  src="EMBED_URL">
</iframe>
```

Replace `EMBED_URL` with the value of the URL examples for your programming 
language. 

### Embed options

-	The `width` and `height` properties are suggestions. Adjust them to the a size 
  that is appropriate for your application.
-	The `frameborder` and `style` properties makes it such that it removes the 
  ordinary iframe border, making the application look better in your application.

## Support

If you have any issues or questions, then send requests to the following:

Email support: irisprodsupport@opynhealth.com
