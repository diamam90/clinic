DO
$do$
BEGIN
    IF EXISTS (
        SELECT FROM pg_catalog.pg_roles
        WHERE rolname = 'developer') THEN
        RAISE NOTICE 'Role "developer" already exists. Skipping';
    ELSE
        BEGIN
            CREATE ROLE developer WITH LOGIN PASSWORD 'developer_password';
        EXCEPTION
            WHEN duplicate_object THEN
                RAISE NOTICE 'Role "developer" already created in a concurrent transaction. Skipping';
        END;
    END IF;
END;
$do$
