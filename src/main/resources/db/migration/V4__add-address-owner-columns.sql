ALTER TABLE address
    ADD COLUMN owner_id UUID;

ALTER TABLE event
    ADD COLUMN address_id UUID;

UPDATE address a
SET owner_id = e.owner_id
FROM event e
WHERE a.event_id = e.id;

UPDATE event e
SET address_id = a.id
FROM address a
WHERE a.event_id = e.id;

ALTER TABLE address
    ALTER COLUMN owner_id SET NOT NULL;

ALTER TABLE address
    DROP CONSTRAINT IF EXISTS fk_address_event,
    DROP CONSTRAINT IF EXISTS address_event_id_key;

ALTER TABLE address
    DROP COLUMN event_id;

ALTER TABLE address
    ADD CONSTRAINT fk_address_owner
        FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE event
    ADD CONSTRAINT fk_event_address
        FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE SET NULL;

CREATE INDEX idx_address_owner_id ON address(owner_id);
CREATE INDEX idx_event_address_id ON event(address_id);
