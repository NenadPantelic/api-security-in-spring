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