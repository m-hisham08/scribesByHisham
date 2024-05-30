-- Insert categories if not exists
INSERT INTO categories (id, name) VALUES (1, 'RELIGION') ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name) VALUES (2, 'TECHNOLOGY') ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name) VALUES (3, 'HISTORY') ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name) VALUES (4, 'PROGRAMMING') ON CONFLICT (id) DO NOTHING;

-- Insert roles if not exists
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN') ON CONFLICT (id) DO NOTHING;
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER') ON CONFLICT (id) DO NOTHING;
