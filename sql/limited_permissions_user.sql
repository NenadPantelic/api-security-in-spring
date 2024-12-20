DO $$ 
BEGIN
  IF EXISTS 
  (
    SELECT
      rolname 
    FROM
      pg_roles 
    WHERE
      rolname = 'apisecurity'
  )
THEN
  EXECUTE 'DROP OWNED BY apisecurity cascade';
END
IF;
END
 $$ ;
 
DROP USER IF EXISTS apisecurity;

CREATE USER apisecurity WITH password 'apisecurity_password';

GRANT CONNECT 
ON database postgres TO apisecurity;

GRANT usage 
ON schema public TO apisecurity;

GRANT 
SELECT, INSERT, UPDATE, DELETE
ON jdbc_customer, jdbc_merchant, jpa_customer, basic_auth_user TO apisecurity;