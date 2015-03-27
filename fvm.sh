#!/bin/bash
fvm -u $1 jwf $2 build/pub/jwf-v$2.jar
fvm -u $1 jwf-src $2 build/pub/jwf-v$2-src.jar
