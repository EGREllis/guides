# guides
Event tracker for a girl guides group

Intended features:

Database:
  Store members
  Store events
  Store payment for a member to go to an event (including payment type: cash or card)
  Distribute receipts (by email or SMS)

Application:
  Add/remove members
  Add/remove events
  Add/remove payments
  Send receipts for a single event

Known bugs:
20200303: Add payment type adds as duplicate records (2, then 3, then 4) (regardless of data model).
20200304: Add payment using direct JDBC implementation throws Exception.
20200304: SELECT single rows for event and payment type throw Exceptions	FIXED

