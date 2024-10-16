DO $$
    begin
        raise notice 'Creating jive_hub db';
    end;
$$;
create database jive_hub;

DO $$
    begin
        raise notice 'Creating jive_hub_test db';
    end;
$$;
create database jive_hub_test;