CREATE TABLE address (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    uf VARCHAR NOT NULL,
    city VARCHAR NOT NULL,
    event_id UUID,
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
);