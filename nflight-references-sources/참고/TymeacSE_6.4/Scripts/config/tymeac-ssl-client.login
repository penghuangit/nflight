/* JAAS login configuration file for client */

com.tymeac.TymeacClient {
    com.sun.security.auth.module.KeyStoreLoginModule required
  keyStoreAlias="tymeacclient"
  keyStoreURL="file:config/tymeacclient.keystore"
  keyStorePasswordURL="file:config/tymeacclient.password";
};
