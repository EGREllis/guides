CREATE TABLE guides.event (
  event_id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  title VARCHAR(16) NOT NULL,
  start_date DATE NOT NULL,
  PRIMARY KEY (event_id)
);