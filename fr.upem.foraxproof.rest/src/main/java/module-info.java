module fr.upem.foraxproof.rest {
    requires spring.boot;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires java.sql;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.fasterxml.jackson.databind;
    opens fr.upem.foraxproof.rest to spring.core;

    exports fr.upem.foraxproof.rest;
}
