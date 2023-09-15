-- dropping enum types
DROP TYPE IF EXISTS TICKET_TYPE_ENUM CASCADE;

-- dropping tables 
DROP TABLE IF EXISTS "User" CASCADE;
DROP TABLE IF EXISTS Coordinates CASCADE;
DROP TABLE IF EXISTS Event CASCADE;
DROP TABLE IF EXISTS Ticket CASCADE;
DROP TABLE IF EXISTS Creator CASCADE;

-- dropping domains
DROP DOMAIN IF EXISTS positive_integer CASCADE;
DROP DOMAIN IF EXISTS price_constraint CASCADE;
DROP DOMAIN IF EXISTS min_age_constraint CASCADE;

-- creating enums if they're exists
CREATE TYPE TICKET_TYPE_ENUM AS ENUM ('VIP', 'USUAL', 'BUDGETARY', 'CHEAP');

-- create domain's
CREATE DOMAIN positive_integer AS INTEGER
CHECK (VALUE > 0);

CREATE DOMAIN price_constraint AS positive_integer;
CREATE DOMAIN min_age_constraint AS positive_integer;

-- creating tables if they're exists
CREATE TABLE IF NOT EXISTS "User" (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    passwd_hash TEXT NOT NULL,
    passwd_salt TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Coordinates (
    id SERIAL PRIMARY KEY,
    x DOUBLE PRECISION CHECK (x > -222) NOT NULL,
    y FLOAT CHECK (y > -274) NOT NULL
);

CREATE TABLE IF NOT EXISTS Event (
    id SERIAL PRIMARY KEY,
    event_name TEXT NOT NULL,
    event_date TIMESTAMP NOT NULL,
    min_age min_age_constraint NOT NULL
);

CREATE TABLE IF NOT EXISTS Ticket (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    coordinates_id INTEGER REFERENCES Coordinates(id) ON DELETE RESTRICT NOT NULL,
    creation_date TIMESTAMP NOT NULL, 
    price price_constraint,
    ticket_type TICKET_TYPE_ENUM NOT NULL,
    event_id INTEGER REFERENCES Event(id) ON DELETE RESTRICT NOT NULL
);


CREATE TABLE IF NOT EXISTS Creator (
    user_id BIGINT DEFAULT 1 REFERENCES "User"(id) ON DELETE SET DEFAULT NOT NULL,
    ticket_id BIGINT REFERENCES Ticket(id) ON DELETE CASCADE NOT NULL UNIQUE,
    PRIMARY KEY (user_id, ticket_id)
);

