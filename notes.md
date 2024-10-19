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
  like drop table or database, create user etc -> POLA (Principle of Least Authority); use the db user with limited access

