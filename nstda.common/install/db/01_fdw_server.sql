CREATE EXTENSION postgres_fdw;
CREATE SERVER foreign_server
        FOREIGN DATA WRAPPER postgres_fdw
        OPTIONS (host '0.0.0.0', port '1111', dbname 'PABI2');
CREATE USER MAPPING FOR alfresco
        SERVER foreign_server
        OPTIONS (user 'erp', password 'xxx');
