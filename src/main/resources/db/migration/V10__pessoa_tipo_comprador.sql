DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type t JOIN pg_enum e ON t.oid=e.enumtypid WHERE t.typname='pessoa_tipo' AND e.enumlabel='COMPRADOR') THEN
    ALTER TYPE pessoa_tipo ADD VALUE 'COMPRADOR';
  END IF;
END $$;