#!/bin/bash
# Start the SMAC evaluation server at port 32100.
java -Xmx10g -cp .:"target/lib/*":target/classes de.julielab.jcore.ae.genemapper.smac.HttpParamOptServer 32100
