#!/bin/sh
java -p lib/foraxproof.impl-1.0.jar:lib/foraxproof.core-1.0.jar:lib/asm-6.0.jar \
     --add-modules fr.upem.foraxproof.impl \
     -jar lib/foraxproof.main-1.0-jar-with-dependencies.jar \
      $@
