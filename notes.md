# API security

## SQL injection

Injecting a dangerous string into an input string. E.g. an application expects to execute

```sql
SELECT name FROM user WHERE email = ...
```

- the following string is injected:

```sql
DROP TABLE user;
```

So, an application executes both.

```
SELECT name FROM user WHERE email = ...
DROP TABLE user;
```

Q: where does the injection happen?
A: in user input

- query param -> api.com/something?param=xxxx
- path variable -> api.com/something/xxxx
- in request body:

```
{
"name": "John",
"email": XXXX
}
```

- the vulnerability can show itself upon the database access if the SQL
  query is created dynamically by concatenating strings
- you are not safe just by using some DAO lib/framework
- is POST HTTP verb safer than GET? Not per se, both can be used to execute SQL injection
- CRUD API is safe from read SQL injection as it used the prepared statements
- yet, it is vulnerable to trigger/procedure executions

#### Prepared statements

- SQL with placeholders
- Replace a placeholder with actual value
- SQL statement is not modified, just the placeholders in template are replaced
- `?` placeholders are filled in with the actual parameters during the SQL execution

```roomsql
SELECT * FROM mytable WHERE email = ? AND role = ?
```

e.g.

```roomsql
SELECT * FROM mytable WHERE email = 'np@ante.lic' AND role = 'ADMIN' 
```

or, in case it contains the dangerous code

```roomsql
SELECT * FROM mytable WHERE email = 'np@ante.lic' AND role = 'ADMIN; DROP TABLE user' 
```

The previous query will not return any data which is bad in terms of business rules, but in technical terms that is much
better than having a malicious query executed.

- Safe code:
    - filter/validate dangerous words and phrases (drop table etc.)
    - filter the request before reaching the actual API method
    - create filter based on regular expression
    - block a dangerous request
    - all URLs or some of them
    - stored procedures can be vulnerable per se, so having them means we would need to maintain them as well; use less
      stored procedures
    - give only required permission for work. E.g. the application db user should not have the unnecessary permissions
      like drop table or database, create user etc -> POLA (Principle of Least Authority); use the db user with limited
      access

- JPA and JDBC might clash
    - JPA - Java Persistence API is an ORM standard (it uses JDBC beneath to read and write to database) that does
      querying + mapping. One implementation of JPA is Hibernate (others are OpenJPA, toplink)
    - JDBC - Java Database Connectivity is a standard for Database access - it does querying and you have to add the
      mapping code by yourself

- SQL injection is one of the OWASP (Open Web Application security project) top 10 vulnerabilities
    - code in the database is vulnerable (stored procedure, trigger)
    - don't write code in the database
    - perfect sql-injection-free code?
    - use prepared statements
    - use principle of least authority
    - sanitize/validate input
    - rotate the database credentials often (Spring + Hashicorp Vault for the credential management)
    - Audi trail (db audit trail/proxy log/code log using filter or Spring AOP)
    - Use WAF (Web application firewall) - built-in features to detect, block and log attack attempts

- Which framework to use in Java
    - JDBC
    - Spring Data JDBC
    - Spring Data JPA
    - MyBatis
- __Don't use string join and dynamic queries__
- R2DBC - Spring Data Reactive library

## XSS

- Cross-site scripting
- injects & run a dangerous scripts (JavaScript) in a web page
- Browser execute a dangerous scripts and the user is sometimes not even aware of it
- Possible scenarios:
    - getting cookie (via document.cookie) - to steal authn/z coookie
    - keylogger (via onkeypress and then send key to attacker)
    - create screenshot and send to attacker
- Types of XSS:
    - Reflective: executed in victim's browser, not saved
    - Persistent: saved & executed every time some function is executed

- XSS in steps:
    1. attacker injects a website with dangerous script
    2. dangerous script from website's HTML page sends an infected HTML to victim
    3. victim's browser executes the code and give benefit to attacker

- XSS script can be shared through an email (you click on some link and tada...). An example:

```
Example 1: <a href="https://www.furyfriend.com/login?ref=[somedangerousscriptcamouflagedaslink]">
Example 2: <a href="https://bit.ry/poodle5">
Actual URL: <a href="https://www.furyfriend.com/login?ref=[somedangerousscriptcamouflagedaslink]">
```

- An example of persistent XSS could be: an attacker found a forum, news website etc. where you can post some articles.
  The attacker can create a malicious post with the embedded JS script that does some harmful thing. Since the article
  is
  saved on server, every visitor that opens that article is going to be a victim of XSS attack.

React frontend code:

```
npx create-react-app frontend
npm install react-router-dom --save
npm install @tinymce/tinymce-react 
```

#### XSS threat

- on input and output
    - input: accept XSS string
    - output: give XSS string as response
- we have to handle both cases
- Input:
    - sanitize input
    - validate input/create filter
    - validate payload content
    - 415 Unsupported Media Type
- Output:
    - encode output
    - set response type Content-Type header
    - XSS prevention response headers
        - X-XSS-Protection: 0 (a legacy thing)
      > The HTTP X-XSS-Protection response header was a feature of Internet Explorer, Chrome and Safari that stopped
      pages from loading when they detected reflected cross-site scripting (XSS) attacks. These protections are largely
      unnecessary in modern browsers when sites implement a strong Content-Security-Policy that disables the use of
      inline JavaScript ('unsafe-inline').
        - X-Content-Type-Options: nosniff (to prevent the browser guessing the correct Content-Type)
          Values:
            - 0 <br>
              Disables XSS filtering.
            - 1 <br>
              Enables XSS filtering (usually default in browsers). If a cross-site scripting attack is detected, the
              browser will sanitize the page (remove the unsafe parts).
            - 1; mode=block <br>
              Enables XSS filtering. Rather than sanitizing the page, the browser will prevent rendering of the page if
              an attack is detected.
            - 1; report=<reporting-URI> (Chromium only) <br>
              Enables XSS filtering. If a cross-site scripting attack is detected, the browser will sanitize the page
              and report the violation. This uses the functionality of the CSP report-uri directive to send a report.
      ```
      Content-Security-Policy: script-src
      'none'; img-src
      https://cdn.company.com
      frame-ancestors 'none';
      ```
      CSP does three things:
        1. prevents the response loading any scripts
        2. allows only the image loading from https://cdn.company.com
        3. prevents the response being loaded into an iframe

## Where to put the security code?

- creating a security-based code in the API itself is not practical since we have to focus on the business logic
- Web application firewall (WAF) which secure more on the network side and API management tools can help in such case
- Some API management tools:
    - apigee
    - axway
    - mashery
    - mulesoft
    - kong
    - ws2 api manager
    - IBM API connect
    - tyk.io
    - ....
- all API calls should go through the API management component. API management has the following duties:
    1. basic auth
    2. rate limiting
    3. max request size
    4. logging
    5. analytics
    6. IP whitelisting
    7. secure by API key
    8. other common functionalities

## Denial of service (DoS)

- prevents legitimate users to access API
- it produces a large amount of traffic to consume API resource
- it can be caused by a single bad client
- it can be also a distributed attack - many IP addresses are involved across the network which makes the situation
  more complex (distributed DoS = DDoS)

- unlike SQL injection and XSS, this attack does not exploit weakness in code, since every system can be a target of DoS
  or DDoS since every system needs some resources to run
- in DDoS an attacker can send the script to many servers, which then executes it and bombards the API of interest - IP
  spoofing
- DoS attacks can be massive, meaning many requests are made - all of them eat a small portion of resources, but due to
  attack's massiveness, the problem arises
- another option is having fewer requests within the rate limit, but all of them are very hungry in terms of resources
  which causes the denial of service (large response payload, inefficient db queries)

### Prevention

#### IP restriction

- allow/reject requests based on IP
- usually implemented in network firewall
- __whitelist__: allow specific IP, close others
    - works in business-to-business apps, where the IPs are known
    - not possible for public API (we do not know all incoming-requests IPs)
    - also, an attacker can use another legitimate IP -> IP spoofing
- __blacklist__: open for everyone, except for specific IPs
    - this is not a prevention, it usually comes after an attack has been executed, so we know the attacker's IP now
    - the problem is, an attacker can use the dynamic IP which always changes

#### Rate limiting

- allow certain number of requests, reject when limit is reached
- prevents the server capacity overloading
- reject some rather than crash all
- retry later when the limit is refreshed or system comes back to normal
- Practically in Java (Google Guava lib)

#### Pagination

- return a small portion of data on each hit
- limit the max amount of data on each hit
- combine with rate limiting

- Linux firewalls: IP tables, firewalld

## Encode, encrypt, hash

### Encoding

- Encode

```
Original string: Today is a rainy day.
Encode using: Base64
Encoded string: VG9kYXkgaXMgYSByYWlueSBkYXku
```

```
Original string: is four+five = nine?
Encode using: URL encode
Encoded string: is%20four%2Bfive%20%3D%20nine%3F
```

Encode:

- not for security!
- Data transformation for proper consumption by other systems
- E.g. images are encoded with Base64, so it can be transferred as printable characters
- on the other side, system can decode it to get the original data
- easy to encode/decode
- just know the encoding
- URL encoding the URL, so special characters like space or question mark which can be misleading
- We don't need to encode the whole URL, just the query parameter

### Encrypt

- used for security
- data can only be consumed by a valid recipient
- using key/password to decrypt (reverse encrypt)
- key/password kept secret
- example: AES, 3DES

```
Original string: Today is a rainy day.
Encrypt using: AES-128-ECB
Secret key: MySecretKey12345
Encrypted string: Encoded as Base64
bxm6YI/1kdJKTtaQu27GcYsf/C+MIElTNxvwZ2oHkrU=
```

```
Original string: is four + five = nine?
Encrypt using: AES-256-CBC
Secret key: MyVeryLongSecretKeyForEncryption
Encrypted string: Encoded as Base64
Hi+7upoOVc4Up+/Whqv1XNGH4WEqvmEJVIpTJwLx+TQ=
```

### Hash

- used for security
- used for data integrity (checksum/signature) - hash some object, piece of data and if something change in the original
  plain object, its hash will be different, meaning something has changes (we don't know what exactly, but that' not
  important)
- cannot be reversed (can't take the original data)
- takes input and produces a fixed-length string
- if two objects are the same, and you hash them, you will get the same hashed value
- two different objects can have the same hashed value, but the chances are very small, even none for good hashing
  algorithms
- example: SHA, Bcrypt, Argon2

```
Original string: Today is a rainy day.
Hash using: SHA-256
Hashed string: 7884def76ea3cd8f918da945ad789b6713d7a94271d42bb8853bf41572afdd54
```

```
Original string: is four + five = nine?
Hash using: BCrypt
No of rounds: 12
Hashed string:
$2a$12$qG.6T6NdBSAnDjW0YWPwCe0Nreq0ntbnU1ufFsfZ16vlSfVT2hCvm
```

- Salt and hash
    - random character(s) added to the original string; represents an extra layer of security
    - how to compare hashed values:
        - we have a plain string, salt and the hashed string
        - hash(plain string + salt ) == hashed string

```
Original string: OhMyPassword
Hash using: SHA-256
Salt (unique): CBXOPM
Original + salt: CBXOPMOhMyPassword
Hashed string: 
2a9f95053926032078157b5f24f854a9a7b9bedaedd46fcf87c3f7b1c6200473
```

### How to protect a password in database?

- use hash with salt
- unique salt for each user to increase the security level
- encryption:
    - an attacker can steal the user database and an encryption key
    - decrypt all user data, get plain passwords
    - many users tend to reuse the same username and password in other places as well
- Hash:
    - not reversible, no plain password
    - better with salt

- Where should I keep my salt?
    - if the salt is per transaction, then it doesn't have to be stored
    - if it is unique per user, then it can be stored in the database

### HMAC - Hash-based Message Authentication code

- it's like a signature sent with some data use to verify that the data has not been altered or replaced -> verify
  message integrity
- HMAC is calculated by a client

Sample rule:

```
message = lowercase(HTTP verb) + \n + URI + \n + body.amount + \n + body.full_name + \n + header.Register-Date
```

- every client usually has its own unique secret key
- `HMAC = HMAC-SHA512(message, secret key)` and it sends HMAC value in header
- the API server knows the rule and the secret key for every consumer; it calculates HMAC based on the same rule
- compares what it received with the locally computed value
    - if they are the same, it knows the message is authentic
    - otherwise, a message is corrupted or tampered

- Since HMAC is hash, what about salt?
- We are using Nonce for that
- Nonce:
    - large sequence number
    - used once (usually on certain time period)
    - add nonce to message construct before hashing
    - the same message, different nonce = different HMAC

### Encode, Encrypt, Hash use case

#### Base64

- can be used to encode a binary file and transfer it over the network
- e.g. an image can be encoded
- downsides:
    - larger size
    - additional process on sender and receiver
    - not saved as base64 (we store the original object usually)

- Recommendations for binary/image transfer:
    - usable, but not best
    - use multipart
    - use URL and download from it

#### URL encode

- useful to encode a URL and transmit some info in it
    - `mysite.com/company?name=Ben/John & Partner`
    - Whitespace, `/`, and `&` are special characters in URL. Might cause unpredictability during processing
    - `mysite.com/company?name=Ben%2FJohn%20%26%20Partner` - we get the safe representation

#### Encrypt/decrypt

- useful when we need to securely transfer the data, but later we need the original data
- example:
    - `api.com/customer?phone=0038164356721`
    - plain phone number, an attacker may get it
    - if we encrypt it -> `api.com/customer?phone=<encrypted>`, we are safe
    - we can use encryption on DB level to keep the data secret (e.g. salary), so even if the database leaks, it is
      useless

### Hash

- Message integrity by giving signature
- check API request vs signature
- message vs signature different: bad message
- HMAC

## Data transmission

### Free Wi-Fi trap

- free wifi in coffee shop
- it might not be owned by the coffee shop
- an attacker setup the free-wifi to perform packet sniffing
- if the user sends some sensitive data like username, password, credit card number etc. to unsecure website, an
  attacker
  may steal it

### Prevention

- HTTPS
    - encrypt the data transmission
    - to get an HTTPS, we must obtain a TLS certificate
    - this certificate is issued by CA (Certificate Authority) - trustworthy 3rd-party entity which is trusted in the
      client-server communication. Browser will only trust certificate by CA (self-signed certificates are not trusted)

- Generating a key with expiry time of 10 years

```
keytool -genkeypair -alias apisecurity -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore apisecurity.p12 -validity 3650
```

- An example of curl with https:

```
curl --request POST \
  --url https://localhost:8080/api/v1/encode/url \
  --header 'Content-Type: application/json' \
  --cookie JSESSIONID=BFF5A546D54F4E1B0523E8DA987BBD43 \
  --data '{
        "text": "is four + five = nine?"
}' -k

```

- instead of having HTTPS applied to all internal services, we can apply it to load balancer or firewall (entrypoints)
  to offload the SSL termination and make it more simple, yet secure
- if we have an API consumed by the browser, use HTTPS
- set HTTP `Strict-Transport-Security (HSTS)`
- `HSTS` response header:

```
Strict-Transport-Security max-age=<age in seconds>; includeSubDomains
```

- saved in the browser, informing about the validity of the certificate and explaining that subdomains should be access
  with HTTPS
- Redirect HTTP to HTTPS on reverse proxy (nginx) and set HSTS header
- Sample nginx configuration

```
server {
  listen 80;
  server_name _;
  add_header Strict-Transport-Security "max-age=31536000; includeSubDomains";
  return 301 https://$host$request_uri;
}

server {
  listen 443;
  server_name _;
  add_header Strict-Transport-Security "max-age=31536000; includeSubDomains";
  ssl_certificate /path/to/certificate;
  ssl_certificate_key /path/to/certificate_key; 
}
```

```
location /https {
    return 200 'Hello there';
    add_header Content-Type text/plain;
}
```

- if you have HSTS header set and the HTTPS endpoint is not present, it will return an error
- Chrome: `chrome://net-internals/#hsts` - to check which domains should be treated only with https

## Audit log

- application log for system trace
- log in security aspect
- audit log: who is doing what
- record attempted operations to find out abuse
- write log to durable storage

### Where to keep log

- text file
- database
- ELK (Elasticsearch, Logstash, Kibana) - free
- Paid: Loggly, Datadog

- who: open audit logs to small group of trusted auditors
- auditors are not system administrators/engineers
- separation of duties
    - different aspect of actions, different people
    - no single person responsible for the entire aspect
- Example: payment requester different with payment approver
- a person who administers the system should not manage the logs (administrator can remove his accesses from logs)

- what to log:
    - timestamp (with enough precision)
    - HTTP method
    - API path
    - query string
    - authentication details (uncovered, no leak)
    - request body

- How:
    - log must be meaningful
    - use specialized tools with logs in JSON format
- Audit log & threat
    - not having it is a threat
    - not just technical
    - wrong suspect > wrong action > additional cost
    - correct suspect > correct action > faster resolution

## Basic authentication

- Identify and verify the user identity
- Basic auth: username & password
- HTTP Basic authentication:
    - HTTP Authorization header
    - Basic base64(username:password)
    - Example:
        - username: anna
        - password: thepassword
        - Basic auth: YW5uYTp0aGVwYXNzd29yZA==
    - Don't do this:
        - inject username and password into the URL: mysite.com/api/yyy?user=anna&password=thepassword
        - base64 encoded string is not a security measure (not a secret)

Practical:

```
user_id: surrotage PK
username: 
encrypted email
- secret key: TheSecretKey2468
- IV: L1v4g8gT1DqjSPB6
Email is lowercase (before encrypt)

password_hash: Hashed password using bcrypt
salt: Salt for hashing
display_name: Name to be shown
```

- Basic auth header -> `Basic <encoded string>`

#### WWW-Authenticate

- Optional WWW-Authenticate response header tells the client which auth mechanism to use, e.g.:

```
WWW-Authenticate: Basic realm="Need a valid credential to access this API"
```

Browser will know to serve the basic auth modal.

## Access Control List

- All for all idea
    - open all endpoints to all users
    - everyone with the correct credential can access the API
    - bad idea
        - endpoints:

        1. POST /admin/user: creates a new user -> should be restricted only to admin
        2. DELETE /admin/user: deletes a user -> should be restricted only to admin
        3. GET /admin/user: get the user details -> should be restricted only to admin
        4. GET /weather: get current weather -> informational, open to everyone
        5. GET /time: get current time -> informational, open to everyone

### Access control list

- Principle of the Least authority
    - enough to do the job
    - too many: damage the system
    - too few: job not done well
- Access Control List:
    - list of user given access to object & permission what to do
    - limit access to endpoints & HTTP method


- An example; we have 4 users and 5 endpoints:
- Users:

1. Sally
2. Admin
3. Anna
4. John

- Endpoints:

1. POST /admin/user: creates a new user -> should be restricted only to admin
2. DELETE /admin/user: deletes a user -> should be restricted only to admin
3. GET /admin/user: get the user details -> should be restricted only to admin
4. GET /weather: get current weather -> informational, open to everyone
5. GET /time: get current time -> informational, open to everyone

- 1, 2, 3 only to Admin
- 4 to Admin, Sally and John
- 5 to everyone

### Authentication & Authorization

- Authentication
    - identify user
    - username, password - basic auth
    - more advanced - OTP, authentication apps, biometric
- Authorization
    - Access control list
    - Grants/revokes access to resource
    - Grants/revokes access to perform an action
- Authentication should be followed by authorization

- 401 Unauthorized -> authentication failure
- 403 Forbidden -> authorization failure

- 401 Unauthorized:
    - failed authentication (no credential, incorrect credential, wrong type - API requires biometric, but receives
      basic)
    - the client can try with another credential (WWW-Authenticate header can be sent in response to inform the client
      about the authentication type it needs)
- 403 Forbidden:
    - credential (authentication) is fine
    - no access to resource/operation

## Token authentication

- Basic authentication drawbacks:
    - sends the plain username and password for each API (prone to stealing the credentials)
    - hash is slow process
        - designed to be slow (to prevent the attacks)
        - a lot of API calls needs more resource (every time it has to hash the password)
        - browser remember username & password
        - not recommended for web apps

- An alternative
    - login endpoint returns the token (valid for xx hours) and keeps it in token store
    - consumer uses then that token as its credential
    - API validates the token

- What could be implemented:
    - token based authentication
    - store as session cookie
    - 15 minutes validity period

- Session cookie
    - session/transient/non-persistent cookie (stored in memory temporarily)
    - temporary while the user navigates the website
    - web browser usually deletes it on close
    - possible to set maximum age to control the expiration even if the browser is not closed

### Session fixation threat

- this type of authentication works, but it is vulnerable
- checks the session -> `request.getSession(true)`
    - no existing session: creates a new session
    - existing session: returns the existing session ID (the same session ID it there was one before that)

1. Attacker logs in and gets the session ID AB56
2. he sends the dangerous link with the session ID AB56 to a victim
3. a victim logs in with his own credentials and because the server has session ID stored it stores victim's credentials
4. now the attacker can use victim's account (credentials) with his session ID

**Prevent session id**

- don't use the fixed session id
- invalidate session

- Cookie security attributes
    - **HttpOnly**: can't be read by JavaScript, so cross-site scripting is not possible
    - **Secure**: cookie can be sent only using HTTPS

### CSRF (Cross Site Request Forgery)

- a victim is logged in to our app
- it gets the session cookie
- while he's logged in, he receives a dangerous link from an attacker
- he goes to that link and attacker will reuse its cookie and send it to our app from his page

Prevent CSRF:

- if UI and API are on the same domain, use **SameSite** cookies
    - cookie will only be sent if the request domain is equal to the original cookie domain
    - dangerous UI (different domain) can't send the cookie
- Proofing that the consumer really knows the session cookie
    - use the token on custom header + session cookie
    - hashed string value (hard to guess) -> hash-based double submit cookie

### Timing attack

- String equality (`String.equals()`) returns on first non-matching character
- the attacker can track the response time, getting in that way the number of characters that do match
- he then arranges the matching character for the legitimate string
- time calculation must be very precise (in nanoseconds); this is theoretically possible but hardly exploitable
  -> factors network latency, context switching, garbage collection takes a few milliseconds etc.
- securing is better
- `MessageDigest.isEqual` is better option
    - compares string bytes
    - calculation time is constant (based on 1st string's length)

## Cross Origin Resource Sharing - CORS

- in browser only
- protection policy - both sides must be on the same domain by default
- restrict scripts from one origin to another

```
mydomain.com ----> api.mydomain.com is blocked per se by Same-Origin Policy (SOP)
```

- origin: scheme-domain-port -> https://sub.domain.com:7777/<<<page/shopping-cart>>>
- Origin policy:
    - allowed HTTP headers
    - allowed HTTP methods (POST, PUT, GET,...)
- Preflight request:
    - the browser sends a small request to server to check if it will accept the actual request
    - OPTIONS method is used (the same URL as the target endpoint) - it sends which origin, headers, method are used...
      and gets 200 or 403

```
https://mydomain.com:5555 ----> https://other.com:8888

mydomain.com
Hi API, I'm https://mydomain.com port 5555. I'd like to send a POST request with HTTP header X-Custom, is it allowed?

server: Yes, go ahead
browser: sends the request
```

- Import response headers:
    - `Access-Control-Allow-Origin`
    - `Access-Control-Allow-Methods`
    - `Access-Control-Allow-Headers`
    - `Access-Control-Allow-Credentials` - browser cookies, password and TLS certificates
    - `Access-Control-Max-Age` - the max number of seconds that browser can cache CORS headers
    - `Access-Control-Expose-Headers` - the list of the headers that can be returned in response


- Spring `CrossOrigin`; default values:
    - Allow all origins - `Access-Control-Allow-Origin: *`
    - Allow specified method on `@XXXMapping` or `@RequestMapping(method = RequestMethod.XXX)`
    - Allow all headers

- `@CrossOrigin` parameters:

```
origins: String[]  -> Access-Control-Allow-Origin
methods: RequestMethod[]  -> Access-Control-Allow-Methods
allowedHeaders: String[]  -> Access-Control-Allow-Headers
allowCredentials: String[]  -> Access-Control-Allow-Credentials
maxAge: long  -> Access-Control-Max-Age
exposedHeaders: String[]  -> Access-Control-Expose-Headers
```

- Can be used on an endpoint level (method), at class level, at global level

- CORS preflight request
- `SameSite` and CORS headers?

## Token without cookies

- Cookies work just fine when the origin is the same, but when we use different origins, things are harder
- which token store to use? Auth token characteristics:
    - validated on each request
    - simple data structure
    - not changed once issued
- we need something fast that can handle these characteristics
- Redis or RDBMS?
- Redis
    - match characteristics (fast, simple data)
    - expired feature
- Relational database
    - No Redis node
    - low traffic
    - we need scheduler to delete the expired tokens

- Authorization bearer -> header `Authorization: Bearer <token>`

- Threats
    - Client: LocalStore: XSS injection (an attacker injects the script to read the local storage)
    - Storage (RDBMS): SQL injection
    - Redis: if unsecured can steal the tokens
- Prevention:
    - token with HMAC
        - do not keep HMAC value in the database (if it gets compromised)
        - verify data integrity
        - verify request token
        - valid token + invalid HMAC = invalid request
        - send in Authorization header: `Bearer myToken.hmac(myToken)` - keep the key used to compute HMAC safely (not
          in
          the database)
- Key storage (tips):
    - keep it in a separate file
    - don't push to repository
    - encrypt the key on file
    - example: jasypt for encryption
    - pro: simple
    - cons: big maintenance effort (if we have several servers where we have to maintain them)
      => centralized database
        - keep it safe (protection level); do not keep it in the source code
        - Key management service - Cloud (AWS, Heroku, Google Cloud...) or our server - Hashicorp Vault

## JSON Web Token (JWT)

- overhead for maintaining the data storage on server
- an alternative:
    - no data storage
    - secure token data
    - send to client
    - client use token for API request
    - data + how to validate available on token

- JWT:
    1. header
    2. payload
    3. signature
- `Header.Payload.Signature`

### Header

- contains the information/metadata about the token itself

```json
{
  "type": "JWT",
  "alg": [
    sign-algorithm
  ]
}
```

- e.g.

```json
{
  "type": "JWT",
  "alg": "HS256"
}
```

- base64 encoded JSON string
- Standard header fields:

1. `alg` - defines HMAC algorithm; e.g. `HS256`
2. `typ` - defines media type. Since we use JWT, usually this has fixed value "JWT"; e.g. `JWT`
3. `kid` - key ID to lookup HMAC keys on server side; e.g. `my-key-1`
4. `jwk` - key for HMAC. Writing this to the header makes JWT vulnerable to attack; e.g. `thisIsMySecretKey`
5. `jku` - URL to retrieve HMAC key. Writing this to the header makes JWT vulnerable;
   e.g. https://mystorage.com/jwt/my-secret-key

- Since we can have multiple keys, all of them should have their own id; with it we know which key is being used

### Payload

- it contains claims (standard ones according to RFC-7519 and custom ones)

```json
{
  "iss": "apisecurity.com",
  // issuer
  "exp": "1640988000"
}
```

- base64 encoded JSON string
- Payload standard claims:

1. `iss` - issuer, who issued the JWT token. Generally application name or authentication provider;
   e.g. `apisecurity.com`
2. `sub` - subject, usually username or other unique identifier of the client; e.g. `elsa@apisecurity.com`
3. `aud` - audience; for whom this token is valid. Usually a URI where server has a set of valid URIs to check against;
   e.g. `https://apisecurity.com`
4. `exp` - expiration time, epoch second when this token will become invalid; e.g. 1643327549
5. `nbf` - not before, JWT token start valid time in epoch second (not valid to use before this moment); e.g. 1640905495

- Private claims:

1. role - user role; e.g. member-diamond
2. amount - purchase amount to be paid; e.g. 2700
3. birthDate - customer birthdate; e.g. 2001-04-20
4. location - latitude & longitude; -6.12324236, 106.6754016459387

- clock skew
    - time validation happens for `nbf` and `exp`
    - clock skew means there is some time difference between nodes (faster or slower time)
    - should give some tolerance (some delta time - a few mins max) when validating timestamp fields; this tolerance
      is called Leeway

### Signature

- `hash(header.payload, secret)` - used to verify the integrity of the token; e.g.
  `HMACSHA256(header-base64.payload-base64, "mySecret123")`

- we don't have to use all predefined JWT fields

#### JWT threats

- in 2015, Tim McClean discovered vulnerabilities in many JWT libraries where the attacker can change the algorithm
  (`alg`) in JWT header, even to none (no need to validate the signature)
- Server does not maintain the token state
- No way for server to revoke the token
- Replay attack: attacker can steal & use valid token
- For that reason, do not use the long-living JWT
- hybrid JWT - store jti (JWT ID) in database and remove it when someone wants to logout (keep it there as long as
  someone
  is logged in); easy to revoke such JWTs

#### JWT, JWS, JWE, JOSE

- JWT is an interface, a standard
- there are two implementations of it:

1. JWS - JSON Web Signature (more common implementation) -> Header.Payload.Signature

- header is also known as JOSE - JavaScript Object Signing and Encryption
- payload can be easily decoded (base64), so we should not add any secret to it

2. JWE - JSON Web Encryption

- consists of 5 parts:

1. Header
2. 2nd JWE part - encrypted key
3. 3rd JWE part - initialization vector (IV)
4. 4th JWE part - ciphertext
5. 5th JWE part - authentication tag

- JWE can also contain the secret data in claims since it encrypts and guards the data in claims
- payload: plain string, json, string, jws payload