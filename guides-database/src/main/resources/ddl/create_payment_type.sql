CREATE TABLE guides.payment_type (
  payment_type_id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  description VARCHAR(16) NOT NULL,
  PRIMARY KEY (payment_type_id)
);