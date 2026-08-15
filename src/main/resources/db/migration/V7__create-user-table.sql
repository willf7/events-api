CREATE TYPE user_role AS ENUM ('ADMIN', 'CUSTOMER', 'MODERATOR');

CREATE TABLE users (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role user_role DEFAULT 'CUSTOMER' NOT NULL
);
