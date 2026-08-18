CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE event (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    img_url VARCHAR(255) NOT NULL,
    event_url VARCHAR(255) NOT NULL,
    date TIMESTAMP WITH TIME ZONE NOT NULL,
    remote BOOLEAN NOT NULL,
    owner_id UUID NOT NULL,
    CONSTRAINT fk_event_owner
        FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE address (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    state VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    event_id UUID NOT NULL UNIQUE,
    CONSTRAINT fk_address_event
        FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
);

CREATE TABLE coupon (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    discount INTEGER NOT NULL,
    valid_until TIMESTAMP WITH TIME ZONE NOT NULL,
    event_id UUID NOT NULL,
    CONSTRAINT fk_coupon_event
        FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
);
