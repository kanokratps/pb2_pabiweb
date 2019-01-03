CREATE EXTENSION postgres_fdw;
CREATE SERVER foreign_server
        FOREIGN DATA WRAPPER postgres_fdw
        OPTIONS (host 'pabi2db.intra.nstda.or.th', port '29005', dbname 'PABI2');
CREATE USER MAPPING FOR alfresco
        SERVER foreign_server
        OPTIONS (user 'odoo', password 'xxx');
