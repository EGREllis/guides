CREATE TABLE registration (
  registration_id INT NOT NULL,
  username VARCHAR(8) NOT NULL,
  password VARCHAR(16) NOT NULL,
  permissions INT NOT NULL,
  PRIMARY KEY (registration_id)
);