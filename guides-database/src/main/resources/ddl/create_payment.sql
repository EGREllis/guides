CREATE TABLE payment (
  payment_id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  client_id INT NOT NULL,
  event_id INT NOT NULL,
  payment_type_id INT NOT NULL,
  payment_date DATE NOT NULL,
  PRIMARY KEY (payment_id),
  CONSTRAINT client_id_fk FOREIGN KEY(client_id) REFERENCES client (client_id),
  CONSTRAINT event_id_fk FOREIGN KEY(event_id) REFERENCES event (event_id),
  CONSTRAINT payment_type_id_fk FOREIGN KEY (payment_type_id) REFERENCES payment_type (payment_type_id)
);