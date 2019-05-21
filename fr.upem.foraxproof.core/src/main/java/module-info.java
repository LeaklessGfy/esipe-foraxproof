module fr.upem.foraxproof.core {
    requires org.objectweb.asm;
    requires java.xml;
    requires java.sql;

    exports fr.upem.foraxproof.core;
    exports fr.upem.foraxproof.core.analysis;
    exports fr.upem.foraxproof.core.event;
    exports fr.upem.foraxproof.core.event.app;
    exports fr.upem.foraxproof.core.event.asm;
    exports fr.upem.foraxproof.core.event.asm.method;
    exports fr.upem.foraxproof.core.runner;
    exports fr.upem.foraxproof.core.handler;
    exports fr.upem.foraxproof.core.exporter;
    exports fr.upem.foraxproof.core.visitor;
}