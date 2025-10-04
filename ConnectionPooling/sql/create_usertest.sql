DROP TABLE IF EXISTS UserTest CASCADE;

CREATE TABLE UserTest (
  UserID SERIAL PRIMARY KEY,
  FirstName VARCHAR(50),
  LastName  VARCHAR(50),
  EmailAddress VARCHAR(100)
);

INSERT INTO UserTest (FirstName, LastName, EmailAddress) VALUES
('John',   'Smith',   'jsmith@gmail.com'),
('Andrea', 'Steelman','andrea@murach.com'),
('Joel',   'Murach',  'joelmurach@yahoo.com');
