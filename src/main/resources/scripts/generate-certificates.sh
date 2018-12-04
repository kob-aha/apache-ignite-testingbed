#!/bin/bash

KEYPASS=$1
STOREPASS=$2
ALIAS="igniteInstance"

if [[ -z ${STOREPASS} || -z ${KEYPASS} ]]; then
    echo "Usage: generate-certificates.sh KEYPASS STOREPASS"
    exit 1
fi

# Generate keystore per instance
keytool -genkey -alias ${ALIAS} \
    -keyalg RSA \
    -keypass ${KEYPASS} \
    -storepass ${STOREPASS} \
    -dname "CN=localhost.instance1, OU=R&D, O=None, L=TLV, S=Israel, C=IL" \
    -keystore instance1.jks
keytool -genkey -alias ${ALIAS} \
    -keyalg RSA \
    -keypass ${KEYPASS} \
    -storepass ${STOREPASS} \
    -dname "CN=localhost.instance2, OU=R&D, O=None, L=TLV, S=Israel, C=IL" \
    -keystore instance2.jks

# Export public keys
keytool -export -alias ${ALIAS} -storepass ${STOREPASS} -file instance1.cer -keystore instance1.jks -keypass ${KEYPASS}
keytool -export -alias ${ALIAS} -storepass ${STOREPASS} -file instance2.cer -keystore instance2.jks -keypass ${KEYPASS}

# Import public keys to a single store used by both instances
keytool -import -v -trustcacerts -alias igniteInstance1 -file instance1.cer -keystore instancescerts.jks -storepass ${STOREPASS}
keytool -import -v -trustcacerts -alias igniteInstance2 -file instance2.cer -keystore instancescerts.jks -storepass ${STOREPASS}