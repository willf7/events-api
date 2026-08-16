CREATE TABLE refresh_tokens (
    id UUID NOT NULL,
    user_id UUID NOT NULL,
    token_hash TEXT NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT(NOW()),
    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
)