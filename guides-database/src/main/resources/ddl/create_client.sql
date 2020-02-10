CREATE TABLE guides.client (
  client_id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  first_name VARCHAR(32) NOT NULL,
  last_name VARCHAR(32) NOT NULL,
  sms VARCHAR(13),
  email VARCHAR(64),
  PRIMARY KEY (client_id)
);