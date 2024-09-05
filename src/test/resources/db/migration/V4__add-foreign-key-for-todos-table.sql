ALTER TABLE todos
ADD CONSTRAINT fk_account
FOREIGN KEY (account_id)
REFERENCES accounts (id);