module DaoProject {
    requires ModelProject;
    requires slf4j.api;
    requires java.sql;
    requires com.h2database;
    opens pl.dk.dao;
    exports pl.dk.dao;
    exports pl.dk.dao.exceptions;
}