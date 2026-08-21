ALTER TABLE coupon
    ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN single_use_per_user BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN max_uses INTEGER,
    ADD COLUMN uses_count INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();

CREATE TABLE coupon_events (
    coupon_id UUID NOT NULL,
    event_id UUID NOT NULL,
    PRIMARY KEY (coupon_id, event_id),
    CONSTRAINT fk_coupon_events_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE CASCADE,
    CONSTRAINT fk_coupon_events_event
        FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
);

INSERT INTO coupon_events (coupon_id, event_id)
SELECT id, event_id
FROM coupon
WHERE event_id IS NOT NULL;

ALTER TABLE coupon
    DROP CONSTRAINT IF EXISTS fk_coupon_event,
    DROP COLUMN event_id;

CREATE TABLE coupon_usage (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    coupon_id UUID NOT NULL,
    user_id UUID NOT NULL,
    code_snapshot VARCHAR(100) NOT NULL,
    discount_snapshot INTEGER NOT NULL,
    valid_until_snapshot TIMESTAMP WITH TIME ZONE NOT NULL,
    used_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_coupon_usage_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE CASCADE,
    CONSTRAINT fk_coupon_usage_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_coupon_events_event_id ON coupon_events(event_id);
CREATE INDEX idx_coupon_usage_coupon_id ON coupon_usage(coupon_id);
CREATE INDEX idx_coupon_usage_user_id ON coupon_usage(user_id);
