/* JAAS login configuration file for server */

com.tymeac.TymeacServer {
    com.sun.security.auth.module.KeyStoreLoginModule required
  keyStoreAlias="tymeacserver"
  keyStoreURL="file:config/tymeacserver.keystore"
  keyStorePasswordURL="file:config/tymeacserver.password";
};
