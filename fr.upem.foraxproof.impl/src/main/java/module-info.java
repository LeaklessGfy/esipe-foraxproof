module fr.upem.foraxproof.impl {
    requires fr.upem.foraxproof.core;
    requires java.xml;
    requires org.objectweb.asm;

    provides fr.upem.foraxproof.core.event.Registrable
            with fr.upem.foraxproof.impl.AbstractRule,
                    fr.upem.foraxproof.impl.ProtectedRule,
                    fr.upem.foraxproof.impl.InstanceOfRule,
                    fr.upem.foraxproof.impl.AnonymousRule,
                    fr.upem.foraxproof.impl.PublicInterfaceRule,
                    fr.upem.foraxproof.impl.LengthRule;
}
